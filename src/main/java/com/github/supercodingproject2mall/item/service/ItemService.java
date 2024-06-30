package com.github.supercodingproject2mall.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.supercodingproject2mall.item.dto.ItemDetailDto;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    public ItemDetailDto getItemDetail(String itemId) throws JsonProcessingException {
        Integer itemIdInt = Integer.valueOf(itemId);
        List<Object[]> results = itemRepository.findItemDetailById(itemIdInt);
        if (results.isEmpty()) {
            return null;
        }
        Object[] result = results.get(0);
        return new ItemDetailDto(
                (String) result[0],
                (Integer) result[1],
                (String) result[2],
                Arrays.asList(((String) result[3]).split(",")),
                Arrays.asList(((String) result[4]).split(","))
        );
    }
}
