package com.github.supercodingproject2mall.item.entity;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.category.entity.CategoryEntity;
import com.github.supercodingproject2mall.itemOption.entity.ItemOptionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
@ToString
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private LocalDate listedDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToMany(mappedBy = "item")
    private Set<CartItemEntity> cartItems;

    @OneToMany(mappedBy = "item")
    private Set<ItemOptionEntity> itemOptions;
}
