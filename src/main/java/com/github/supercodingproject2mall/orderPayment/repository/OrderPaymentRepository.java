package com.github.supercodingproject2mall.orderPayment.repository;

import com.github.supercodingproject2mall.orderPayment.entity.OrderPaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPaymentEntity,Integer> {
}
