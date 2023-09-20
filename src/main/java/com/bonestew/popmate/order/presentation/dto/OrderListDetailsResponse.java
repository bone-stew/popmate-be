package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.Order;
import com.bonestew.popmate.order.domain.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public record OrderListDetailsResponse(
    OrderListDetailResponse orderListDetailResponse
) {
    public static OrderListDetailsResponse from(
        Order order
    ){
        OrderListDetailResponse response = OrderListDetailResponse.from(order);
        return new OrderListDetailsResponse(
            response
        );
    }
}
