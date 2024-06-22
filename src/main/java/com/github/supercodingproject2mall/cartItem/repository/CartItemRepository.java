package com.github.supercodingproject2mall.cartItem.repository;

import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity,Integer> {
}
