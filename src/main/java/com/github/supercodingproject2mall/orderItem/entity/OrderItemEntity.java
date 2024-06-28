package com.github.supercodingproject2mall.orderItem.entity;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.order.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
@Builder
public class OrderItemEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_per_unit")
    private Integer pricePerUnit;
}
