package com.github.supercodingproject2mall.itemSize.repository;

import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemSizeRepository extends JpaRepository<ItemSizeEntity, Integer> {
        ItemSizeEntity findByItemIdAndOptionSize(Integer itemId, String optionSize);
        List<String> findOptionSizeByItemId(Integer itemId);
}