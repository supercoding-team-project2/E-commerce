package com.github.supercodingproject2mall.img.repository;

import com.github.supercodingproject2mall.img.entity.ImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImgRepository extends JpaRepository<ImgEntity,Integer> {
    String findUrlByItemId(Integer itemId);
}
