package com.github.supercodingproject2mall.item.entity;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
@ToString
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @Column(name="price", nullable = false)
    private Integer price;

    @Column(name = "total_stock", nullable = false)
    private Integer totalStock;

    @Column(name = "listed_date", nullable = false)
    private LocalDate listedDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private UserEntity seller;

    @Column(name = "category_gender", length = 10,  nullable = false)
    private String categoryGender;

    @Column(name = "category_kind", length = 10, nullable = false)
    private String categoryKind;

    @OneToMany(mappedBy = "itemId")
    private Set<ItemSizeEntity> itemSizes;

    @OneToMany(mappedBy = "item")
    private Set<CartItemEntity> cartItems;

    @ElementCollection
    @CollectionTable(name = "item_images", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "image_url", length = 1000)
    private List<String> imageUrls;

}