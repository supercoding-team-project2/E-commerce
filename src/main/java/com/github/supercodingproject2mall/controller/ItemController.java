package com.github.supercodingproject2mall.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.supercodingproject2mall.item.dto.ItemDetailDto;
import com.github.supercodingproject2mall.item.service.ItemService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/api")
public class ItemController {
    private final ItemService itemService;
    @GetMapping("/item/{itemId}")
    public ResponseEntity<ItemDetailDto> getItemDetail (@PathVariable String itemId) throws JsonProcessingException {
        ItemDetailDto itemDetail = itemService.getItemDetail(itemId);
        if (itemDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(itemDetail);
    }
}
