package com.bonestew.popmate.order.presentation.dto;

public record OrderResponse(
    Long orderId
) {
    public static OrderResponse from(
        Long orderId
    ){
        return new OrderResponse(
            orderId
        );
    }
}
