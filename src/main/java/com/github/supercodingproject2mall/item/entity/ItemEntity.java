package com.github.supercodingproject2mall.item.entity;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
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

    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "name", length = 1000)
    private String description;

    @Column(name="price")
    private Integer price;

    @Column(name = "stock_stock")
    private Integer stock;

    @Column(name = "listed_date")
    private LocalDate listedDate;

    @Column(name = "category_gender")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private UserEntity seller;

    @Column(name = "category_gender")
    private String categoryGender;

    @Column(name = "category_kind")
    private String categoryKind;

    @OneToMany(mappedBy = "item")
    private Set<CartItemEntity> cartItems;

}
