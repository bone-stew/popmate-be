package com.bonestew.popmate.order.presentation.dto;

import java.time.LocalDateTime;

public record OrderListItemResponse(
    Long orderId,
    Long userId,
    Long storeId,
    LocalDateTime orderDate,
    int status,
    int total_amount,
    LocalDateTime createdAt
) {


}
