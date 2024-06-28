package com.github.supercodingproject2mall.itemOption.repository;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.itemOption.entity.ItemOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemOptionRepository extends JpaRepository<ItemOptionEntity, Integer> {
}
