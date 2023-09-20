package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public record OrderListDetailResponse(
    LocalDateTime createdAt,
    Long orderId,
    Long userId,
    Long popupStoreId,
    String title,
    String placeDetail,
    String bannerImgUrl,
    int total_amount,
    int status,
    String orderTossId,
    String url,
    String cardType,
    String easyPay,
    String method,
    String qrImgUrl,
    List<OrderItem> orderItemList
) {
    public static OrderListDetailResponse from(
        Order order
    ){
        return new OrderListDetailResponse(
            order.getCreatedAt(),
            order.getOrderId(),
            order.getUser().getUserId(),
            order.getPopupStore().getPopupStoreId(),
            order.getPopupStore().getTitle(),
            order.getPopupStore().getPlaceDetail(),
            order.getPopupStore().getBannerImgUrl(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getOrderTossId(),
            order.getUrl(),
            order.getCardType(),
            order.getEasyPay(),
            order.getMethod(),
            order.getQrImgUrl(),
            order.getOrderItemList()
        );
    }
}
