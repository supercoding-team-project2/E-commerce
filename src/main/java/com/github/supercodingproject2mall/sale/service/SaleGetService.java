package com.github.supercodingproject2mall.sale.service;

import com.github.supercodingproject2mall.itemSize.dto.ItemSizeDto;
import com.github.supercodingproject2mall.sale.dto.SaleGetDto;
import com.github.supercodingproject2mall.sale.repository.SaleGetRepository;
import com.github.supercodingproject2mall.itemSize.repository.ItemSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleGetService {

    private final SaleGetRepository saleGetRepository;
    private final ItemSizeRepository itemSizeRepository;

    public List<SaleGetDto> getCurrentSaleItems(Integer sellerId) {
        List<SaleGetDto> saleGetDtos = saleGetRepository.findCurrentSaleItemsBySellerId(sellerId);
        return saleGetDtos.stream()
                .peek(dto -> dto.setSizes(getItemSizes(dto.getId())))
                .collect(Collectors.toList());
    }

    private List<ItemSizeDto> getItemSizes(Integer itemId) {
        List<Object[]> sizeEntities = itemSizeRepository.findSizesByItemId(itemId);
        return sizeEntities.stream()
                .map(size -> new ItemSizeDto((String) size[0], (Integer) size[1]))
                .collect(Collectors.toList());
    }
}
