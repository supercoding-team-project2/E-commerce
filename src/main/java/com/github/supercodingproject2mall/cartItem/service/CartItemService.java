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
            List<String> urls = imgRepository.findUrlByItemId(cartItemEntity.getItem());
            String url = urls.isEmpty()? null : urls.get(0);

            Integer totalPrice = cartItemEntity.getItem().getPrice() * cartItemEntity.getQuantity();
            List<String> optionSize = itemSizeRepository.findOptionSizeByItemId(cartItemEntity.getItem());

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


    private CartItemEntity cartRequestToCartItemEntity(Integer userCartId, CartRequest cartRequest) {
        CartEntity cartEntity = cartRepository.findById(userCartId)
                .orElseThrow(()-> new NotFoundException("해당 ID: "+ userCartId+"의 카트를 찾을 수 없습니다."));
        ItemEntity itemEntity = itemRepository.findById(cartRequest.getItemId())
                .orElseThrow(()-> new NotFoundException("해당 ID: "+cartRequest.getItemId()+"의 아이템을 찾을 수 없습니다."));

        if(cartRequest.getSize() == null){
            ItemSizeEntity itemSizeEntity = itemSizeRepository.findByItemId(itemEntity);
            return CartItemEntity
                    .builder()
                    .cart(cartEntity)
                    .item(itemEntity)
                    .quantity(cartRequest.getQuantity())
                    .itemSize(itemSizeEntity)
                    .build();
        }
        ItemSizeEntity itemSizeEntity = itemSizeRepository.findByItemIdAndOptionSize(itemEntity, cartRequest.getSize());
        return CartItemEntity
                .builder()
                .cart(cartEntity)
                .item(itemEntity)
                .quantity(cartRequest.getQuantity())
                .itemSize(itemSizeEntity)
                .build();
    }
}
