package com.github.supercodingproject2mall.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.supercodingproject2mall.img.repository.ImgRepository;
import com.github.supercodingproject2mall.item.dto.AllItemDto;
import com.github.supercodingproject2mall.item.dto.ItemDetailDto;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ImgRepository imgRepository;

    public ItemDetailDto getItemDetail(String itemId) throws JsonProcessingException {
        Integer itemIdInt = Integer.valueOf(itemId);
        List<Object[]> results = itemRepository.findItemDetailById(itemIdInt);
        if (results.isEmpty()) {
            return null;
        }
        Object[] result = results.get(0);
        return new ItemDetailDto(
                (String) result[0],
                (Integer) result[1],
                (String) result[2],
                Arrays.asList(((String) result[3]).split(",")),
                Arrays.asList(((String) result[4]).split(","))
        );
    }

    public Page<AllItemDto> getItemByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if(category.equals("ALL")){
            Page<ItemEntity> itemEntities = itemRepository.findAll(pageable);
            return new PageImpl<>(itemEntitiesToAllItemDto(itemEntities),pageable,itemEntities.getTotalElements());
        } else {
            Page<ItemEntity> itemEntities = itemRepository.findAllByCategory(pageable, category);
            return new PageImpl<>(itemEntitiesToAllItemDto(itemEntities),pageable,itemEntities.getTotalElements());
        }
    }
    private List<AllItemDto> itemEntitiesToAllItemDto(Page<ItemEntity> itemEntities){
        List<AllItemDto> allItems = new ArrayList<>();
        for (ItemEntity itemEntity : itemEntities.getContent()) {
            List<String> urls = imgRepository.findUrlByItemId(itemEntity);
            String url = urls.isEmpty()? null : urls.get(0);

            AllItemDto allItem = AllItemDto.builder()
                    .id(itemEntity.getId())
                    .name(itemEntity.getName())
                    .price(itemEntity.getPrice())
                    .url(url)
                    .build();
            allItems.add(allItem);
        }
        return allItems;
    }
}
