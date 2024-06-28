package com.github.supercodingproject2mall.item.repository;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity , Integer> {
}
