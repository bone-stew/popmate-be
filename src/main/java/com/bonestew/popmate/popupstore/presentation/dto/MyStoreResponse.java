package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;

public record MyStoreResponse(Long popupStoreId, String title) {
    public static MyStoreResponse from(PopupStore popupStore) {
        return new MyStoreResponse(popupStore.getPopupStoreId(), popupStore.getTitle());
    }
}
