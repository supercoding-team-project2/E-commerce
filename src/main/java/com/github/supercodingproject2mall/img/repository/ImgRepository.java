package com.github.supercodingproject2mall.img.repository;

import com.github.supercodingproject2mall.img.entity.ImgEntity;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImgRepository extends JpaRepository<ImgEntity,Integer> {

    @Query("SELECT i.url FROM ImgEntity i WHERE i.item = :itemId ORDER BY i.id ASC")
    List<String> findUrlByItemId(ItemEntity itemId);
}
