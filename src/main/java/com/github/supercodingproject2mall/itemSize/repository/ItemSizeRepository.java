package com.github.supercodingproject2mall.itemSize.repository;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemSizeRepository extends JpaRepository<ItemSizeEntity, Integer> {

        @Query("SELECT i FROM ItemSizeEntity i WHERE i.itemId = :itemId")
        ItemSizeEntity findByItemId(ItemEntity itemId);

        @Query("SELECT i FROM ItemSizeEntity i WHERE i.itemId = :item AND i.optionSize = :optionSize")
        ItemSizeEntity findByItemIdAndOptionSize(ItemEntity item, String optionSize);

        @Query("SELECT i.optionSize FROM ItemSizeEntity i WHERE i.itemId = :itemId")
        List<String> findOptionSizeByItemId(ItemEntity itemId);

        List<ItemSizeEntity> findAllByItemId(ItemEntity item);

        @Query("SELECT is.optionSize, is.stock FROM ItemSizeEntity is WHERE is.itemId.id = :itemId")
        List<Object[]> findSizesByItemId(@Param("itemId") Integer itemId);

        @Query("SELECT SUM(is.stock) FROM ItemSizeEntity is WHERE is.itemId.id = :itemId")
        int sumStockByItemId(@Param("itemId") Integer itemId);
}