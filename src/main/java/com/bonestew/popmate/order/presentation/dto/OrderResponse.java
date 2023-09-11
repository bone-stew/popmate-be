package com.bonestew.popmate.order.presentation.dto;

public record OrderResponse(
    String message
) {
    public static OrderResponse from(
        String message
    ){
        return new OrderResponse(
            message = message
        );
    }
}
