package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStoreItem;

public record PopupStoreItemResponse(
        Long popupStoreItemId,
        String imgUrl,
        String name,
        int amount
) {

    public static PopupStoreItemResponse from(
            PopupStoreItem popupStoreItem
    ) {
        return new PopupStoreItemResponse(
                popupStoreItem.getPopupStoreItemId(),
                popupStoreItem.getImgUrl(),
                popupStoreItem.getName(),
                popupStoreItem.getAmount()
        );
    }
}
