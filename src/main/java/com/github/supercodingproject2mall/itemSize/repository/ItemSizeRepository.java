package com.github.supercodingproject2mall.itemSize.repository;

import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSizeRepository extends JpaRepository<ItemSizeEntity, Integer> {
}