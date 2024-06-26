package com.github.supercodingproject2mall.auth.repository;

import com.github.supercodingproject2mall.auth.entity.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends JpaRepository<RefreshEntity, Integer> {
}
