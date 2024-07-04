package com.github.supercodingproject2mall.sale.service;

import com.github.supercodingproject2mall.sale.dto.SalePutDto;
import com.github.supercodingproject2mall.sale.repository.SalePutRepository;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SalePutService {

    private final SalePutRepository salePutRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void updateItemStockAndPrice(SalePutDto salePutDto, Integer sellerId) {
        if (!salePutRepository.isSellerOfItem(salePutDto.getItemId(), sellerId)) {
            throw new RuntimeException("해당 사용자는 이 상품의 재고를 업데이트할 권한이 없습니다.");
        }

        String categoryKind = getCategoryKind(salePutDto.getItemId());

        if ("bag".equalsIgnoreCase(categoryKind)) {
            int updatedRows = salePutRepository.updateItemStock(salePutDto.getItemId(), null, salePutDto.getNewStock());
            if (updatedRows == 0) {
                throw new RuntimeException("재고 업데이트 실패.");
            }
            salePutRepository.deleteZeroStockSizesWithOptionSize(salePutDto.getItemId());
        } else {
            int updatedRows = salePutRepository.updateItemStock(salePutDto.getItemId(), salePutDto.getOptionSize(), salePutDto.getNewStock());
            if (updatedRows == 0) {
                throw new RuntimeException("재고 업데이트 실패.");
            }
            salePutRepository.deleteZeroStockSizes(salePutDto.getItemId());
        }

        if (salePutDto.getNewPrice() != null) {
            int updatedPriceRows = salePutRepository.updateItemPrice(salePutDto.getItemId(), salePutDto.getNewPrice());
            if (updatedPriceRows == 0) {
                throw new RuntimeException("가격 업데이트 실패.");
            }
        }

        if (salePutDto.getCategoryGender() != null) {
            int updatedGenderRows = salePutRepository.updateCategoryGender(salePutDto.getItemId(), salePutDto.getCategoryGender());
            if (updatedGenderRows == 0) {
                throw new RuntimeException("성별 업데이트 실패.");
            }
        }

        updateTotalStock(salePutDto.getItemId());
    }

    private void updateTotalStock(Integer itemId) {
        int totalStock = salePutRepository.sumStockByItemId(itemId);
        itemRepository.updateTotalStock(itemId, totalStock);
    }

    private String getCategoryKind(Integer itemId) {
        return salePutRepository.getCategoryKind(itemId);
    }
}
