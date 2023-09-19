package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.Order;
import java.util.List;

public record BackOrderListItemsResponse(
    List<BackOrderListItemResponse> orderListItemResponses
) {
    public static BackOrderListItemsResponse from(
        List<Order> order
    ){
        List<BackOrderListItemResponse> response = order.stream()
            .map(BackOrderListItemResponse::from)
            .toList();
        return new BackOrderListItemsResponse(
            response
        );
    }

}
