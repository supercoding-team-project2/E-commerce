package com.github.supercodingproject2mall.category.entity;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categoies")
@ToString
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    private Set<ItemEntity> items;
}
