package com.github.supercodingproject2mall.sale.dto;

import com.github.supercodingproject2mall.itemSize.dto.ItemSizeDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"imageFiles", "id"})
public class SalePostDto {

    private String name;
    private String description;
    private int price;
    private int totalStock;
    private String categoryGender;
    private String categoryKind;
    private LocalDate listedDate;
    private LocalDate endDate;
    private List<ItemSizeDto> itemSizes;
    private List<String> imageUrls;
    private List<MultipartFile> imageFiles;

    public SalePostDto(String name, String description, int price, int totalStock,
                       String categoryGender, String categoryKind, LocalDate listedDate, LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.totalStock = totalStock;
        this.categoryGender = categoryGender;
        this.categoryKind = categoryKind;
        this.listedDate = listedDate;
        this.endDate = endDate;

        initializeItemSizesByCategory();
        this.imageUrls = new ArrayList<>();
        this.imageFiles = new ArrayList<>();
    }

    private void initializeItemSizesByCategory() {
        if ("bag".equalsIgnoreCase(categoryKind)) {
            this.itemSizes = new ArrayList<>();
            itemSizes.add(new ItemSizeDto(null, 0));
        } else if ("shoes".equalsIgnoreCase(categoryKind)) {
            initializeShoesSizes();
        } else if ("apparel".equalsIgnoreCase(categoryKind)) {
            initializeApparelSizes();
        } else if ("cap".equalsIgnoreCase(categoryKind)) {
            initializeCapSizes();
        } else {
            this.itemSizes = new ArrayList<>();
        }
    }

    private void initializeShoesSizes() {
        this.itemSizes = new ArrayList<>();
        for (int size = 220; size <= 280; size += 5) {
            itemSizes.add(new ItemSizeDto(String.valueOf(size), 0));
        }
    }

    private void initializeApparelSizes() {
        this.itemSizes = new ArrayList<>();
        itemSizes.add(new ItemSizeDto("Small", 0));
        itemSizes.add(new ItemSizeDto("Medium", 0));
        itemSizes.add(new ItemSizeDto("Large", 0));
    }

    private void initializeCapSizes() {
        this.itemSizes = new ArrayList<>();
        itemSizes.add(new ItemSizeDto("Small", 0));
        itemSizes.add(new ItemSizeDto("Medium", 0));
        itemSizes.add(new ItemSizeDto("Large", 0));
    }

    public void addSizeWithStock(String size, int stock) {
        if (itemSizes == null) {
            itemSizes = new ArrayList<>();
        }
        itemSizes.add(new ItemSizeDto(size, stock));
    }

    public Integer getId() {
        return null;
    }

    public void setId(Integer id) {
    }

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
