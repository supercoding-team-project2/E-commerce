package com.github.supercodingproject2mall.order.repository;

import com.github.supercodingproject2mall.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {
    @Query(value = "SELECT JSON_ARRAYAGG(" +
            "JSON_OBJECT('order_id', o.id, 'total_price', o.total_price, 'order_date', o.order_date, " +
            "'items', (SELECT JSON_ARRAYAGG(JSON_OBJECT('item_id', oi.item_id, 'quantity', oi.quantity, 'price_per_unit', oi.price_per_unit, 'name', i.name)) " +
            "FROM order_items oi " +
            "JOIN items i ON oi.item_id = i.id " +
            "WHERE oi.order_id = o.id))) AS order_details " +
            "FROM orders o " +
            "WHERE o.user_id = ?1 " +
            "GROUP BY o.id, o.total_price, o.order_date", nativeQuery = true)
    String findOrdersWithItemsByUserId(Integer userId);
}
