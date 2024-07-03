package com.github.supercodingproject2mall.img.entity;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "imgs")
public class ImgEntity {
    @Id @Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "url", nullable = false, length = 1000)
    private String url;
}
