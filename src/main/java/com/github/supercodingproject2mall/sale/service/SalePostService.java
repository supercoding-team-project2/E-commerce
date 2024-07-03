package com.github.supercodingproject2mall.sale.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.img.entity.ImgEntity;
import com.github.supercodingproject2mall.img.repository.ImgRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import com.github.supercodingproject2mall.itemSize.dto.ItemSizeDto;
import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import com.github.supercodingproject2mall.itemSize.repository.ItemSizeRepository;
import com.github.supercodingproject2mall.sale.dto.SalePostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalePostService {

    private final ItemRepository itemRepository;
    private final ItemSizeRepository itemSizeRepository;
    private final ImgRepository imgRepository;
    private final UserRepository userRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Transactional
    public SalePostDto addSaleItem(SalePostDto salePostDto, Integer userId, List<MultipartFile> images) {
        UserEntity seller = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID: " + userId + " 의 유저를 찾을 수 없습니다."));

        // ItemEntity 생성
        ItemEntity itemEntity = ItemEntity.builder()
                .name(salePostDto.getName())
                .description(salePostDto.getDescription())
                .price(salePostDto.getPrice())
                .totalStock(salePostDto.getTotalStock())
                .listedDate(salePostDto.getListedDate())
                .endDate(salePostDto.getEndDate())
                .seller(seller)
                .categoryGender(salePostDto.getCategoryGender())
                .categoryKind(salePostDto.getCategoryKind())
                .build();

        ItemEntity savedItem = itemRepository.save(itemEntity);

        saveItemSizes(salePostDto, savedItem);

        List<String> imageUrls = uploadImages(images, savedItem.getId());
        salePostDto.setImageUrls(imageUrls);

        updateTotalStock(savedItem.getId());

        salePostDto.setId(savedItem.getId());
        return salePostDto;
    }

    private void saveItemSizes(SalePostDto salePostDto, ItemEntity savedItem) {
        String categoryKind = salePostDto.getCategoryKind();
        List<ItemSizeDto> itemSizes = salePostDto.getItemSizes();

        if ("bag".equalsIgnoreCase(categoryKind)) {
            ItemSizeEntity itemSizeEntity = ItemSizeEntity.builder()
                    .itemId(savedItem)
                    .optionSize(null)
                    .stock(salePostDto.getTotalStock())
                    .build();

            itemSizeRepository.save(itemSizeEntity);

        } else if ("shoes".equalsIgnoreCase(categoryKind)) {
            for (ItemSizeDto sizeDto : itemSizes) {
                ItemSizeEntity itemSizeEntity = ItemSizeEntity.builder()
                        .itemId(savedItem)
                        .optionSize(sizeDto.getSize())
                        .stock(sizeDto.getStock())
                        .build();

                itemSizeRepository.save(itemSizeEntity);
            }
        } else if ("apparel".equalsIgnoreCase(categoryKind) || "cap".equalsIgnoreCase(categoryKind)) {
            for (ItemSizeDto sizeDto : itemSizes) {
                if ("Small".equalsIgnoreCase(sizeDto.getSize()) ||
                        "Medium".equalsIgnoreCase(sizeDto.getSize()) ||
                        "Large".equalsIgnoreCase(sizeDto.getSize())) {
                    ItemSizeEntity itemSizeEntity = ItemSizeEntity.builder()
                            .itemId(savedItem)
                            .optionSize(sizeDto.getSize())
                            .stock(sizeDto.getStock())
                            .build();

                    itemSizeRepository.save(itemSizeEntity);
                }
            }
        } else {
            for (ItemSizeDto sizeDto : itemSizes) {
                ItemSizeEntity itemSizeEntity = ItemSizeEntity.builder()
                        .itemId(savedItem)
                        .optionSize(sizeDto.getSize())
                        .stock(sizeDto.getStock())
                        .build();

                itemSizeRepository.save(itemSizeEntity);
            }
        }
    }

    private List<String> uploadImages(List<MultipartFile> images, Integer itemId) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile image : images) {
            try {
                String imageUrl = uploadImageToS3(image);
                saveImageToDatabase(itemId, image.getOriginalFilename(), imageUrl);
                imageUrls.add(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return imageUrls;
    }

    private String uploadImageToS3(MultipartFile image) throws IOException {
        String fileName = generateFileName(image);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(image.getContentType());
        metadata.setContentLength(image.getSize());

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, image.getInputStream(), metadata));
        return amazonS3.getUrl(bucketName, fileName).toString();
    }

    private String generateFileName(MultipartFile multipartFile) {
        return UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
    }

    @Transactional
    protected void saveImageToDatabase(Integer itemId, String originalFilename, String imageUrl) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 상품을 찾을 수 없습니다: " + itemId));

        ImgEntity imgEntity = new ImgEntity();
        imgEntity.setItem(item);
        imgEntity.setName(originalFilename);
        imgEntity.setUrl(imageUrl);

        imgRepository.save(imgEntity);
    }

    @Transactional
    public void updateTotalStock(Integer itemId) {
        int totalStock = itemSizeRepository.sumStockByItemId(itemId);
        itemRepository.updateTotalStock(itemId, totalStock);
    }
}
