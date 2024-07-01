package com.github.supercodingproject2mall.itemSize.entity;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_sizes")
public class ItemSizeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity itemId;

    @Column(name = "option_size", length = 10, nullable = false)
    private String optionSize;

    @Column(name = "stock", nullable = false)
    private Integer stock;
}
