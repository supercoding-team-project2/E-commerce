package com.github.supercodingproject2mall.cartOptionValue.repository;

import com.github.supercodingproject2mall.cartOptionValue.entity.CartOptionValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartOptionValueRepository extends JpaRepository<CartOptionValueEntity,Integer> {
}
