package com.github.supercodingproject2mall.cartItem.repository;

import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity,Integer> {
    List<CartItemEntity> findByCartId(Integer cartId);

    @Query("SELECT i FROM CartItemEntity i WHERE i.id = :id")
    CartItemEntity findCartIdById(Integer id);
}
