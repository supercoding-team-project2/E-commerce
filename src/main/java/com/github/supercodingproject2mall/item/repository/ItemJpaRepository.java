package com.github.supercodingproject2mall.item.repository;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemJpaRepository extends JpaRepository<ItemEntity, Integer> {
}
