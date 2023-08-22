package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;

public record PopupStoreResponse(
    Long id,
    String name,
    String description
) {

    public static PopupStoreResponse from(PopupStore popupStore) {
        return new PopupStoreResponse(popupStore.getPopupStoreId(), popupStore.getTitle(), popupStore.getDescription());
    }
}
