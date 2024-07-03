package com.github.supercodingproject2mall.cartItem.service;

import com.github.supercodingproject2mall.cart.dto.CartRequest;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.dto.GetCartItem;
import com.github.supercodingproject2mall.cartItem.dto.UpdateCartRequest;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.repository.CartItemRepository;
import com.github.supercodingproject2mall.img.repository.ImgRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import com.github.supercodingproject2mall.itemSize.repository.ItemSizeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        log.info("유저의 카트 번호 cartId: {}", cartEntity.getId());
        if(cartEntity == null){
            return new ArrayList<>();
        }
        List<CartItemEntity> cartItemEntities = cartItemRepository.findByCartId(cartEntity.getId());
        List<GetCartItem> cartItems = new ArrayList<>();

        for(CartItemEntity cartItemEntity : cartItemEntities){
            log.info("유저의 카트아이템 cartItemEntity: {}", cartItemEntity);
            List<String> urls = imgRepository.findUrlByItemId(cartItemEntity.getItem());
            log.info("아이템 url: {}", urls);
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
            log.info("조회할 카트아이템 :{}", getCartItem);
            cartItems.add(getCartItem);
        }
        return cartItems;
    }

    public void deleteUserCartItem(Integer userId, String cartItemId) {
        Integer cartItemIdInt = Integer.parseInt(cartItemId);
        CartItemEntity deletecartItem =  cartItemRepository.findById(cartItemIdInt)
                .orElseThrow(()->new NotFoundException("삭제할 cartItem을 찾을 수 없습니다."));
        log.info("받아온 삭제할 cartItem = {}", deletecartItem.getCart());

        CartEntity cartEntity = cartRepository.findByUserId(userId)
                .orElseThrow(()-> new NotFoundException("해당 ID: " + userId+"유저의 카트를 찾을 수 없습니다."));
        log.info("삭제할 cartItem이 유저의 카트가 맞는지 확인 cartEntityId= {}, cartEntityUserId ={} ", cartEntity.getId(),cartEntity.getUser());

        if(!cartEntity.getId().equals(deletecartItem.getCart().getId())){
            log.info("token으로 받아온 user의 cartId={}, param으로 받아온 cartItemId={}" ,cartEntity.getId(),cartItemId);
            throw  new NotFoundException("삭제할 cartItem은 유저의 장바구니 내역이 아닙니다.");
        }
        cartItemRepository.deleteById(cartItemIdInt);
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

    public void updateCart(Integer userId, UpdateCartRequest updateCartRequest) {
        CartItemEntity cartItemEntity = cartItemRepository.findById(Integer.parseInt(updateCartRequest.getCartItemId()))
                .orElseThrow(() -> new NotFoundException("수정할 cartItem을 찾을 수 없습니다."));

        //user의 cartItem이 맞는지 확인
        CartEntity cartEntity = cartRepository.findByUserId(userId)
                .orElseThrow(()-> new NotFoundException("해당 ID: " + userId+"유저의 카트를 찾을 수 없습니다."));
        if(!cartEntity.getId().equals(cartItemEntity.getCart().getId())){
            throw  new NotFoundException("수정할 cartItem은 유저의 장바구니 내역이 아닙니다.");
        }

        ItemSizeEntity updateItemSize = new ItemSizeEntity();
        //수정할 아이템 찾기
        ItemEntity itemEntity = cartItemEntity.getItem();
        //수정할 아이템의 사이즈를 찾아 itemsizeEntity의 id 가져오기
        List<ItemSizeEntity> itemSizeEntities = itemSizeRepository.findAllByItemId(itemEntity);
        log.info("수정할 아이템의 모든 사이즈 옵션 ItemSizeEntities = {}", itemSizeEntities);
        for(ItemSizeEntity itemSizeEntity : itemSizeEntities){
            log.info("수정할 updateCartRequest ItemSize ={}, itemsSizeEntity = {}",updateCartRequest.getItemSize(),itemSizeEntity.getOptionSize());
            if(updateCartRequest.getItemSize().equals(itemSizeEntity.getOptionSize())){
                updateItemSize = itemSizeEntity;
                break;
            }
        }

        CartItemEntity updateCartItem = cartItemEntity.toBuilder()
                .quantity(updateCartRequest.getQuantity())
                .itemSize(updateItemSize)
                .build();

        cartItemRepository.save(updateCartItem);
    }
}
