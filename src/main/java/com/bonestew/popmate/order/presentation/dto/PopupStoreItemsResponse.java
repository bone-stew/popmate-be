package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;

public record PopupStoreItemsResponse(
    List<PopupStoreItemResponse> popupStoreItemResponse

){

    public static PopupStoreItemsResponse from(
        List<PopupStoreItem> popupStoreItem
    ) {
        List<PopupStoreItemResponse> response = popupStoreItem.stream()
            .map(PopupStoreItemResponse::from)
            .toList();
        return new PopupStoreItemsResponse(
            response
        );
    }
}

