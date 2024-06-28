package com.github.supercodingproject2mall.cartItem.service;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.UpdateCartRequest;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.repository.CartItemRepository;
import com.github.supercodingproject2mall.cartItemOption.entity.CartItemOptionEntity;
import com.github.supercodingproject2mall.cartItemOption.repository.CartItemOptionRepository;
import com.github.supercodingproject2mall.cartOptionValue.entity.CartOptionValueEntity;
import com.github.supercodingproject2mall.cartOptionValue.repository.CartOptionValueRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import com.github.supercodingproject2mall.itemOption.entity.ItemOptionEntity;
import com.github.supercodingproject2mall.itemOption.repository.ItemOptionRepository;
import com.github.supercodingproject2mall.optionValue.entity.OptionValueEntity;
import com.github.supercodingproject2mall.optionValue.repository.OptionValueRepository;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final OptionValueRepository optionValueRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final CartOptionValueRepository cartOptionValueRepository;

    //TODO: transactional했는데 예외발생해도 cartItem Id 적용됨 해결해야됨
    @Transactional(rollbackOn = Exception.class)
    public void addItemToCart(Integer userCartId, CartRequest cartRequest) {

        CartItemEntity cartItemEntity = cartRequestToCartItemEntity(userCartId,cartRequest);

        cartItemRepository.save(cartItemEntity);

        if(cartRequest.getItemOptionId() != null){

            for (Integer itemOptionId : cartRequest.getItemOptionId()){
                ItemOptionEntity itemOptionEntity = itemOptionRepository.findById(itemOptionId)
                        .orElseThrow(()-> new NotFoundException("해당 ID: "+itemOptionId+"의 아이템 옵션을 찾을 수 없습니다."));

                if (!itemOptionEntity.getItem().getId().equals(cartRequest.getItemId())) {
                    throw new IllegalArgumentException("해당 아이템의 옵션이 아닙니다.");
                }

                CartItemOptionEntity cartItemOptionEntity = cartRequestToCartItemOption(cartItemEntity,itemOptionEntity);
                cartItemOptionRepository.save(cartItemOptionEntity);
            }

            for (Integer optionValueId : cartRequest.getOptionValueId()){
                OptionValueEntity optionValueEntity = optionValueRepository.findById(optionValueId)
                        .orElseThrow(()-> new NotFoundException("해당 ID: "+optionValueId+"의 아이템 옵션값을 찾을 수 없습니다."));

                CartOptionValueEntity cartOptionValueEntity = cartRequestToCartOptionValue(cartItemEntity,optionValueEntity);
                cartOptionValueRepository.save(cartOptionValueEntity);
            }
        }
    }

    @Transactional
    public void updateCart(UpdateCartRequest updateCartRequest) {

        Integer cartItemId = Integer.valueOf(updateCartRequest.getCartItemId());

        //cartItem 조회 by cartItemId
        CartItemEntity cartItemEntity = cartItemRepository.findById(cartItemId)
                .orElseThrow(()->new NotFoundException("해당 ID:"+updateCartRequest.getCartItemId()+"의 cartItem을 찾을 수 없습니다."));

        //cartItemEntity 수량 수정
        CartItemEntity updatedCartItemEntity = cartItemEntity.toBuilder()
                .quantity(updateCartRequest.getQuantity())
                .build();
        cartItemRepository.save(updatedCartItemEntity);


        //update 대상 cartOptionValue 조회
        List<CartOptionValueEntity> cartOptionValueEntities = cartOptionValueRepository.findByCartItemId(cartItemId);
        if (cartOptionValueEntities.isEmpty()) {
            throw new NotFoundException("cart_item_id: " + cartItemId + "를 가진 cartOptionValue를 찾을 수 없습니다.");
        }
        // update 옵션 리스트로 저장
        List<Integer> newOptionValueIds = updateCartRequest.getOptionValueId();
        for(int i=0; i<newOptionValueIds.size(); i++){
            CartOptionValueEntity cartOptionValueEntity = cartOptionValueEntities.get(i);
            OptionValueEntity optionValueEntity = optionValueRepository.findById(newOptionValueIds.get(i))
                    .orElseThrow(()->new NotFoundException("수정할 아이템 옵션값을 찾을 수 없습니다."));
            CartOptionValueEntity updatedCartOptionValueEntity = cartOptionValueEntity.toBuilder()
                    .optionValue(optionValueEntity)
                    .build();
            //cart_item_items_options에 수정된 값 저장
            cartOptionValueRepository.save(updatedCartOptionValueEntity);
        }
    }

    private CartOptionValueEntity cartRequestToCartOptionValue(CartItemEntity cartItemEntity, OptionValueEntity optionValueEntity) {
        return CartOptionValueEntity
                .builder()
                .cartItem(cartItemEntity)
                .optionValue(optionValueEntity)
                .build();
    }

    private CartItemOptionEntity cartRequestToCartItemOption(CartItemEntity cartItemEntity, ItemOptionEntity itemOptionEntity) {
        return CartItemOptionEntity
                .builder()
                .cartItem(cartItemEntity)
                .itemOption(itemOptionEntity)
                .build();
    }

    private CartItemEntity cartRequestToCartItemEntity(Integer userCartId, CartRequest cartRequest) {
        CartEntity cartEntity = cartRepository.findById(userCartId)
                .orElseThrow(()-> new NotFoundException("해당 ID: "+ userCartId+"의 카트를 찾을 수 없습니다."));
        ItemEntity itemEntity = itemRepository.findById(cartRequest.getItemId())
                .orElseThrow(()-> new RuntimeException("해당 ID: "+cartRequest.getItemId()+"의 아이템을 찾을 수 없습니다."));

        return CartItemEntity
                .builder()
                .cart(cartEntity)
                .item(itemEntity)
                .quantity(cartRequest.getQuantity())
                .build();
    }

}
