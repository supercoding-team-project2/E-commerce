package com.github.supercodingproject2mall.img.entity;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imgs")
public class ImgEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ItemEntity item;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;
}
