package com.github.supercodingproject2mall.itemSize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSizeRepository extends JpaRepository<com.github.supercodingproject2mall.itemSizes.entity.ItemSizeEntity, Integer> {
}