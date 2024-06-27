package com.github.supercodingproject2mall.orderItem.repository;

import com.github.supercodingproject2mall.orderItem.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity,Integer> {
}
