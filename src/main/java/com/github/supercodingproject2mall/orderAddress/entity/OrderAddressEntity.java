package com.github.supercodingproject2mall.orderAddress.entity;

import com.github.supercodingproject2mall.order.entity.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_addresses")
public class OrderAddressEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "shipping_address", length = 100)
    private String shippingAddress;

    @Column(name = "city", length = 70)
    private String city;

    @Column(name = "state_province", length = 50)
    private String stateProvince;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

}
