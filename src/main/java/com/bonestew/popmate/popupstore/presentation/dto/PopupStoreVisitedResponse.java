package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;

public record PopupStoreVisitedResponse(
        Long popupStoreId,
        String title,
        String bannerImgUrl

) {


    public static PopupStoreVisitedResponse from(PopupStore popupStore) {
        return new PopupStoreVisitedResponse(
                popupStore.getPopupStoreId(),
                popupStore.getTitle(),
                popupStore.getBannerImgUrl()
        );
    }
}
