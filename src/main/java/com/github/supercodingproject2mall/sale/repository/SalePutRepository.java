package com.github.supercodingproject2mall.sale.repository;

import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SalePutRepository extends JpaRepository<ItemSizeEntity, Integer> {

    @Query("SELECT COALESCE(SUM(is.stock), 0) FROM ItemSizeEntity is WHERE is.itemId.id = :itemId")
    int sumStockByItemId(Integer itemId);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN TRUE ELSE FALSE END FROM ItemEntity i WHERE i.id = :itemId AND i.seller.id = :sellerId")
    boolean isSellerOfItem(Integer itemId, Integer sellerId);

    @Query("SELECT i.categoryKind FROM ItemEntity i WHERE i.id = :itemId")
    String getCategoryKind(Integer itemId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ItemSizeEntity is WHERE is.itemId.id = :itemId AND is.stock = 0")
    void deleteZeroStockSizes(Integer itemId);

    @Modifying
    @Transactional
    @Query("UPDATE ItemSizeEntity is SET is.stock = :newStock WHERE is.itemId.id = :itemId AND (:optionSize IS NULL OR is.optionSize = :optionSize)")
    int updateItemStock(Integer itemId, String optionSize, Integer newStock);

    @Modifying
    @Transactional
    @Query("DELETE FROM ItemSizeEntity is WHERE is.itemId.id = :itemId AND is.stock = 0 AND is.optionSize IS NOT NULL")
    void deleteZeroStockSizesWithOptionSize(Integer itemId);
}
