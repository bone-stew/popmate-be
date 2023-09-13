package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.OrderPlaceDetail;

public record OrderPlaceDetailResponse(
    String title,
    String placeDetail,
    String placeDescription
) {
    public static OrderPlaceDetailResponse from(
        OrderPlaceDetail orderPlaceDetail
    ){
        return  new OrderPlaceDetailResponse(
            orderPlaceDetail.getTitle(),
            orderPlaceDetail.getPlaceDetail(),
            orderPlaceDetail.getPlaceDescription()
        );
    }
}
