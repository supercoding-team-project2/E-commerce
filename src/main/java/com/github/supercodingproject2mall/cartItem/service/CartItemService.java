package com.github.supercodingproject2mall.cartItem.service;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.dto.UpdateCartRequest;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.dto.CartItemResponse;
import com.github.supercodingproject2mall.cartItem.dto.GetCartItem;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.repository.CartItemRepository;
import com.github.supercodingproject2mall.img.repository.ImgRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import com.github.supercodingproject2mall.itemSize.repository.ItemSizeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final ItemSizeRepository itemSizeRepository;
    private final ImgRepository imgRepository;

    public void addItemToCart(Integer userCartId, CartRequest cartRequest) {

        CartItemEntity cartItemEntity = cartRequestToCartItemEntity(userCartId,cartRequest);
        cartItemRepository.save(cartItemEntity);
    }

    public List<GetCartItem> getUserCart(Integer userId){
        CartEntity cartEntity = cartRepository.findByUserId(userId)
                .orElse(null);
        if(cartEntity == null){
            return new ArrayList<>();
        }
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByCartId(cartEntity.getId());
        List<GetCartItem> cartItems = new ArrayList<>();
        for(CartItemEntity cartItemEntity : cartItemEntities){
            String url = imgRepository.findUrlByItemId(cartItemEntity.getItem().getId());
            Integer totalPrice = cartItemEntity.getItem().getPrice() * cartItemEntity.getQuantity();
            List<String> optionSize = itemSizeRepository.findOptionSizeByItemId(cartItemEntity.getItem().getId());

            GetCartItem getCartItem = GetCartItem.builder()
                    .cartItemId(cartItemEntity.getId())
                    .itemUrl(url)
                    .itemName(cartItemEntity.getItem().getName())
                    .itemSize(cartItemEntity.getItemSize().getOptionSize())
                    .quantity(cartItemEntity.getQuantity())
                    .totalPrice(totalPrice)
                    .itemPrice(cartItemEntity.getItem().getPrice())
                    .optionSize(optionSize)
                    .build();

            cartItems.add(getCartItem);
        }
        return cartItems;
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
                .orElseThrow(()-> new NotFoundException("해당 ID: "+cartRequest.getItemId()+"의 아이템을 찾을 수 없습니다."));

        ItemSizeEntity itemSizeEntity = itemSizeRepository.findByItemIdAndOptionSize(itemEntity.getId(), cartRequest.getSize());

        return CartItemEntity
                .builder()
                .cart(cartEntity)
                .item(itemEntity)
                .quantity(cartRequest.getQuantity())
                .itemSize(itemSizeEntity)
                .build();
    }

}
