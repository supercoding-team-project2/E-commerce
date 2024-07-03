package com.github.supercodingproject2mall.cartItem.entity;

import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name = "cart_items")
public class CartItemEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_size_id", nullable = false)
    private ItemSizeEntity itemSize;
}
