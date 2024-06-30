package com.github.supercodingproject2mall.cart.repository;

import com.github.supercodingproject2mall.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    Optional<CartEntity> findByUserId(Integer cartId);

    @Query(value = "SELECT " +
            "i.name AS itemName, " +
            "i.price AS price, " +
            "MIN(img.url) AS imageURL, " +
            "ci.quantity AS quantity, " +
            "isz.option_size AS size " +
            "FROM carts c " +
            "JOIN cart_items ci ON c.id = ci.cart_id " +
            "JOIN items i ON ci.item_id = i.id " +
            "JOIN item_sizes isz ON ci.item_size_id = isz.id " +
            "LEFT JOIN imgs img ON i.id = img.item_id " +
            "WHERE c.user_id = :userId " +
            "GROUP BY i.id, ci.id, isz.option_size, ci.quantity, i.price " +
            "ORDER BY i.name",
            nativeQuery = true)
    List<Object[]> findCartDetailsByUserId(Integer userId);
}
