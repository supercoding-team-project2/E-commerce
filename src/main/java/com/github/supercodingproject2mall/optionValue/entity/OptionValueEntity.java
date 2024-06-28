package com.github.supercodingproject2mall.optionValue.entity;

import com.github.supercodingproject2mall.itemOption.entity.ItemOptionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "option_values")
@ToString
public class OptionValueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String valueName;
    private BigDecimal additionalCost;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private ItemOptionEntity itemOption;
}
