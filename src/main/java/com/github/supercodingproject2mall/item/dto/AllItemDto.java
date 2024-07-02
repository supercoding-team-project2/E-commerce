package com.github.supercodingproject2mall.item.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllItemDto {
    private String name;
    private Integer price;
    private String url;
}
