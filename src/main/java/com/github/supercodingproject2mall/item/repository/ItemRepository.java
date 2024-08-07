package com.github.supercodingproject2mall.item.repository;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity , Integer> {
    @Query(value = "SELECT " +
            "i.name AS itemName, " +
            "i.price AS itemPrice, " +
            "i.description AS itemDescription, " +
            "GROUP_CONCAT(DISTINCT CONCAT(isz.option_size, ': ', isz.stock) ORDER BY isz.option_size) AS sizeOptionsWithStock, " +
            "GROUP_CONCAT(DISTINCT img.url ORDER BY img.id) AS imageUrls " +
            "FROM items i " +
            "LEFT JOIN item_sizes isz ON i.id = isz.item_id " +
            "LEFT JOIN imgs img ON i.id = img.item_id " +
            "WHERE i.id = :itemId " +
            "GROUP BY i.id, i.name, i.price, i.description", nativeQuery = true)
    List<Object[]> findItemDetailById(@Param("itemId") Integer itemId);

    @Query("SELECT i FROM ItemEntity i WHERE i.totalStock != 0")
    Page<ItemEntity> findAll(Pageable pageable);

    @Query("SELECT i FROM ItemEntity i WHERE (i.categoryGender = :category or i.categoryKind =: category) and i.totalStock !=0")
    Page<ItemEntity> findAllByCategory(Pageable pageable, String category);

        @Transactional
        @Modifying
        @Query("UPDATE ItemEntity i SET i.totalStock = :totalStock WHERE i.id = :itemId")
        void updateTotalStock(@Param("itemId") Integer itemId, @Param("totalStock") int totalStock);

        @Transactional
        @Modifying
        @Query("UPDATE ItemEntity i SET i.imageUrls = :imageUrl WHERE i.id = :itemId")
        void updateImageUrl(@Param("itemId") Integer itemId, @Param("imageUrl") String imageUrl);
    }