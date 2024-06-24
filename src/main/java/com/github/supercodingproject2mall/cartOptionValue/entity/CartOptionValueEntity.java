package com.github.supercodingproject2mall.cartOptionValue.entity;

import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.optionValue.entity.OptionValueEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item_option_values")
public class CartOptionValueEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItemEntity cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_value_id", nullable = false)
    private OptionValueEntity optionValue;
}
