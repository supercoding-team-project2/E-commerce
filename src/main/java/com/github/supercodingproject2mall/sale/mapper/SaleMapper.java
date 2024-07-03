package com.github.supercodingproject2mall.sale.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.supercodingproject2mall.sale.dto.SalePostDto;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {

    private final ObjectMapper objectMapper;

    public SaleMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public SalePostDto mapJsonToSalePostDto(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, SalePostDto.class);
    }
}