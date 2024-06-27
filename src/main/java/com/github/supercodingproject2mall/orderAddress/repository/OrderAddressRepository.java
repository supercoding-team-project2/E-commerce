package com.github.supercodingproject2mall.orderAddress.repository;

import com.github.supercodingproject2mall.orderAddress.entity.OrderAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAddressRepository extends JpaRepository<OrderAddressEntity,Integer> {
}
