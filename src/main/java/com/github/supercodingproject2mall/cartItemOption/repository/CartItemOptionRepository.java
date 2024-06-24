package com.github.supercodingproject2mall.cartItemOption.repository;

import com.github.supercodingproject2mall.cartItemOption.entity.CartItemOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemOptionRepository extends JpaRepository<CartItemOptionEntity,Integer> {

}
