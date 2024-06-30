package com.github.supercodingproject2mall.order.service;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.repository.CartItemRepository;
import com.github.supercodingproject2mall.img.entity.ImgEntity;
import com.github.supercodingproject2mall.img.repository.ImgRepository;
import com.github.supercodingproject2mall.order.dto.GetOrderItemResponse;
import com.github.supercodingproject2mall.order.dto.GetOrderRequest;
import com.github.supercodingproject2mall.order.dto.GetOrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ImgRepository imgRepository;
    private final UserRepository userRepository;

    public GetOrderResponse getOrderCart(Integer userId, GetOrderRequest getOrderRequest) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("주문할 유저의 정보를 찾을 수 없습니다."));

        ArrayList<GetOrderItemResponse> orderItemResponses = new ArrayList<>();

        List<Integer> getCartItemIds = getOrderRequest.getCartItemId();
        for(Integer getCartItemId : getCartItemIds) {
            CartItemEntity cartItemEntity = cartItemRepository.findById(getCartItemId)
                    .orElseThrow(()->new NotFoundException("주문할 cartItem을 찾을 수 없습니다."));

            //이미지 조회
            List<String> urls = imgRepository.findUrlByItemId(cartItemEntity.getItem());
            String url = urls.isEmpty()? null : urls.get(0);

            GetOrderItemResponse orderItemResponse = GetOrderItemResponse.builder()
                    .itemUrl(url)
                    .itemName(cartItemEntity.getItem().getName())
                    .itemSize(cartItemEntity.getItemSize().getOptionSize())
                    .itemQuantity(cartItemEntity.getQuantity())
                    .itemPrice(cartItemEntity.getItem().getPrice())
                    .build();

            orderItemResponses.add(orderItemResponse);
        }
        GetOrderResponse getOrderResponse = GetOrderResponse.builder()
                .username(userEntity.getName())
                .phoneNumber(userEntity.getPhoneNum())
                .email(userEntity.getEmail())
                .address(userEntity.getAddress())
                .orderItems(orderItemResponses)
                .build();

        return getOrderResponse;
    }
}
