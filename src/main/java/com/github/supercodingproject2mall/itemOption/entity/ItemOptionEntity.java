package com.github.supercodingproject2mall.itemOption.entity;

import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.optionValue.entity.OptionValueEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items_options")
@ToString
public class ItemOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String optionName;
    private BigDecimal additionalCost;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @OneToMany(mappedBy = "itemOption")
    private Set<OptionValueEntity> optionValues;
}
