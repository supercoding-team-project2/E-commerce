package com.github.supercodingproject2mall.order.service;

import com.github.supercodingproject2mall.auth.entity.UserEntity;
import com.github.supercodingproject2mall.auth.repository.UserRepository;
import com.github.supercodingproject2mall.cart.entity.CartEntity;
import com.github.supercodingproject2mall.cart.repository.CartRepository;
import com.github.supercodingproject2mall.cartItem.entity.CartItemEntity;
import com.github.supercodingproject2mall.cartItem.repository.CartItemRepository;
import com.github.supercodingproject2mall.img.entity.ImgEntity;
import com.github.supercodingproject2mall.img.repository.ImgRepository;
import com.github.supercodingproject2mall.item.entity.ItemEntity;
import com.github.supercodingproject2mall.item.repository.ItemRepository;
import com.github.supercodingproject2mall.itemSize.entity.ItemSizeEntity;
import com.github.supercodingproject2mall.itemSize.repository.ItemSizeRepository;
import com.github.supercodingproject2mall.order.dto.*;
import com.github.supercodingproject2mall.order.entity.OrderEntity;
import com.github.supercodingproject2mall.order.exception.InsufficientStockException;
import com.github.supercodingproject2mall.order.repository.OrderRepository;
import com.github.supercodingproject2mall.orderItem.entity.OrderItemEntity;
import com.github.supercodingproject2mall.orderItem.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ImgRepository imgRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemSizeRepository itemSizeRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    public GetOrderResponse getOrderCart(Integer userId, List<Integer> cartItemIds) {

        log.info("주문할 유저의 ID={}",userId);
        log.info("주문 카트아이템 {}", cartItemIds);
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("주문할 유저의 정보를 찾을 수 없습니다."));

        ArrayList<GetOrderItemResponse> orderItemResponses = new ArrayList<>();

        //List<String> getCartItemIds = getOrderRequest.getCartItemId();
        for(Integer getCartItemId : cartItemIds) {
            CartItemEntity cartItemEntity = cartItemRepository.findById(getCartItemId)
                    .orElseThrow(()->new NotFoundException("주문할 cartItem을 찾을 수 없습니다."));

            //이미지 조회
            List<String> urls = imgRepository.findUrlByItemId(cartItemEntity.getItem());
            String url = urls.isEmpty()? null : urls.get(0);

            GetOrderItemResponse orderItemResponse = GetOrderItemResponse.builder()
                    .cartItemId(cartItemEntity.getId())
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

    @Transactional
    public Integer upLoadOrder(Integer userId, UploadOrderRequest uploadOrderRequest) {
        UserEntity orderUser = userRepository.findById(userId)
                .orElseThrow(()-> new NotFoundException("주문자 정보를 찾을 수 없습니다."));

        //주문번호 만들어서 order 생성
        List<String> orderNumbers = orderRepository.findOrderNumber();
        Set<String> setOrderNumber = new HashSet<>(orderNumbers);
        Random random = new Random();
        String orderNumber;
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                int digit = random.nextInt(10);
                sb.append(digit);
            }
            orderNumber = sb.toString();
        } while (setOrderNumber.contains(orderNumber));

        OrderEntity orderEntity = OrderEntity.builder()
                .user(orderUser)
                .totalPrice(uploadOrderRequest.getTotalPrice())
                .orderDate(LocalDateTime.now())
                .orderNumber(orderNumber)
                .build();
        orderRepository.save(orderEntity);

        List<InsufficientItem> insufficientItems = new ArrayList<>();
        // 수량 확인
        List<Integer> cartItemIds = uploadOrderRequest.getCartItemId();
        for(Integer cartItemId : cartItemIds) {
            CartItemEntity cartItemEntity = cartItemRepository.findById(cartItemId)
                    .orElseThrow(()->new NotFoundException("주문할 cartItem을 찾을 수 없습니다."));

            //주문할 수량이 아이템의 재고보다 크면 주문 안됨
            Integer quantity = cartItemEntity.getQuantity();
            if(quantity > cartItemEntity.getItemSize().getStock()){
                insufficientItems.add(new InsufficientItem(cartItemEntity.getItem().getName(), cartItemEntity.getItemSize().getStock()));
                if (!insufficientItems.isEmpty()) {
                    throw new InsufficientStockException("재고가 부족합니다.",insufficientItems);
                }
                continue;
            }else {
                //옵션별 감소한 재고 저장
                ItemSizeEntity postOrderItemSize = itemSizeRepository.findById(cartItemEntity.getItemSize().getId())
                        .orElseThrow(()-> new NotFoundException("주문할 상품의 옵션을 찾을 수 없습니다."));
                Integer postOrderStock = postOrderItemSize.getStock() - quantity;
                postOrderItemSize = postOrderItemSize.toBuilder()
                        .stock(postOrderStock)
                        .build();
                itemSizeRepository.save(postOrderItemSize);

                //총 재고
                ItemEntity postOrderItemEntity = itemRepository.findById(cartItemEntity.getItem().getId())
                        .orElseThrow(()-> new NotFoundException("주문할 item을 찾을 수 없습니다."));

                postOrderItemEntity = postOrderItemEntity.toBuilder()
                        .totalStock(postOrderItemEntity.getTotalStock()-quantity)
                        .build();
                itemRepository.save(postOrderItemEntity);
            }

            //한 주문에 있는 아이템들 order_items에 저장
            //order_item 저장
            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(orderEntity)
                    .item(cartItemEntity.getItem())
                    .quantity(cartItemEntity.getQuantity())
                    .pricePerUnit(cartItemEntity.getItem().getPrice())
                    .build();
            orderItemRepository.save(orderItem);

            cartItemRepository.deleteById(cartItemId);
        }
        return orderEntity.getId();
    }

    public GetOrderSuccess successOrder(Integer userId, String orderId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(()->new NotFoundException("주문에 성공한 해당 유저를 찾을 수 없습니다."));
        OrderCustomerInfo orderCustomerInfo = OrderCustomerInfo.builder()
                .name(userEntity.getName())
                .phoneNumber(userEntity.getPhoneNum())
                .email(userEntity.getEmail())
                .build();

        OrderEntity orderEntity = orderRepository.findById(Integer.valueOf(orderId))
                .orElseThrow(()-> new NotFoundException("해당 주문을 찾을 수 없습니다."));
        GetOrderSuccess orderSuccess = GetOrderSuccess.builder()
                .orderNumber(orderEntity.getOrderNumber())
                .orderDate(orderEntity.getOrderDate())
                .totalPrice(orderEntity.getTotalPrice())
                .customerInfo(orderCustomerInfo)
                .build();
        return orderSuccess;
    }
}
