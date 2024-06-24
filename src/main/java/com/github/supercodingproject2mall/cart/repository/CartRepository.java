package com.github.supercodingproject2mall.cart.repository;

import com.github.supercodingproject2mall.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    Optional<CartEntity> findByUserId(Integer cartId);
}
