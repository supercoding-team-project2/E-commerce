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

    @Query(value = "SELECT i.name AS item_name, " +
            "ci.quantity, " +
            "GROUP_CONCAT(DISTINCT io.option_name ORDER BY io.option_name SEPARATOR ', ') AS option_names, " +
            "GROUP_CONCAT(DISTINCT ov.value_name ORDER BY ov.value_name SEPARATOR ', ') AS option_values " +
            "FROM users u " +
            "JOIN carts c ON u.id = c.user_id " +
            "JOIN cart_items ci ON c.id = ci.cart_id " +
            "JOIN items i ON ci.item_id = i.id " +
            "LEFT JOIN cart_items_item_options ciio ON ci.id = ciio.cart_items_id " +
            "LEFT JOIN items_options io ON ciio.items_options_id = io.id " +
            "LEFT JOIN cart_items_option_values ciov ON ci.id = ciov.cart_items_id " +
            "LEFT JOIN option_values ov ON ciov.option_values_id = ov.id " +
            "WHERE u.id = ?1 " +
            "GROUP BY ci.cart_id, ci.item_id, ci.quantity, i.name", nativeQuery = true)
    List<Object[]> findCartDetailsByUserId(Integer userId);
}
