package com.github.supercodingproject2mall.sale.repository;

import com.github.supercodingproject2mall.sale.dto.SaleGetDto;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleGetRepository extends JpaRepository<ItemEntity, Integer> {

    @Query("SELECT new com.github.supercodingproject2mall.sale.dto.SaleGetDto(" +
            "i.name, i.description, i.price, i.categoryGender, i.categoryKind, " +
            "i.listedDate, i.endDate, i.id, " +
            "(SELECT img.url FROM ImgEntity img WHERE img.item.id = i.id ORDER BY img.id ASC LIMIT 1)) " +
            "FROM ItemEntity i WHERE i.seller.id = :sellerId AND i.endDate > CURRENT_DATE " +
            "ORDER BY i.listedDate DESC")
    List<SaleGetDto> findCurrentSaleItemsBySellerId(@Param("sellerId") Integer sellerId);
}
