package com.github.supercodingproject2mall.order.entity;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Builder
public class OrderEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "order_email", length = 100)
    private String orderEmail;
}
