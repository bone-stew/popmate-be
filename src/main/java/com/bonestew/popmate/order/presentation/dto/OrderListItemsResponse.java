package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.Order;
import java.time.LocalDateTime;
import java.util.List;

public record OrderListItemsResponse(
    List<OrderListItemResponse> orderListItemResponses
) {
    public static OrderListItemsResponse from(
        List<Order> order
    ){
        List<OrderListItemResponse> response = order.stream()
            .map(OrderListItemResponse::from)
            .toList();
        return new OrderListItemsResponse(
            response
        );
    }

}
