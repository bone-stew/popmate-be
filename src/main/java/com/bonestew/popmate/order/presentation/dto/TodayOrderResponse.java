package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderStatus;
import java.time.LocalDateTime;

public record TodayOrderResponse(
    Long orderId,
    Long userId,
    String userName,
    String orderNumber,
    String orderStatus,
    LocalDateTime pickupTime
) {

    public static TodayOrderResponse from(Order order) {
        return new TodayOrderResponse(
            order.getOrderId(),
            order.getUser().getUserId(),
            order.getUser().getName(),
            order.getOrderTossId(),
            OrderStatus.from(order.getStatus()).getDescription(),
            order.getCreatedAt()
        );
    }
}
