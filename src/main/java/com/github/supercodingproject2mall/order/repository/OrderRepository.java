package com.github.supercodingproject2mall.order.repository;

import com.github.supercodingproject2mall.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    @Query(value = "SELECT JSON_ARRAYAGG(" +
            "JSON_OBJECT(" +
            "'order_id', o.id, " +
            "'order_number', o.order_number, " +
            "'total_price', o.total_price, " +
            "'order_date', o.order_date, " +
            "'items', (SELECT JSON_ARRAYAGG(" +
            "JSON_OBJECT(" +
            "'item_id', oi.item_id, " +
            "'quantity', oi.quantity, " +
            "'price_per_unit', oi.price_per_unit, " +
            "'name', i.name, " +
            "'image_url', (SELECT url FROM imgs WHERE item_id = oi.item_id LIMIT 1)" +
            ")) FROM order_items oi JOIN items i ON oi.item_id = i.id WHERE oi.order_id = o.id GROUP BY oi.order_id)" +
            ")) AS order_details " +
            "FROM orders o " +
            "WHERE o.user_id = :userId " +
            "GROUP BY o.id, o.order_date, o.total_price", nativeQuery = true)
    String findOrdersWithItemsByUserId(Integer userId);
}
