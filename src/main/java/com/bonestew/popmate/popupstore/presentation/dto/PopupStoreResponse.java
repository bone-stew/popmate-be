package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.time.LocalDateTime;

public record PopupStoreResponse(
        Long popupStoreId,
        String title,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        String placeDetail,
        String bannerImgUrl,
        String organizer
) {


    public static PopupStoreResponse from(PopupStore popupStore) {
        return new PopupStoreResponse(
                popupStore.getPopupStoreId(),
                popupStore.getTitle(),
                popupStore.getOpenDate(),
                popupStore.getCloseDate(),
                popupStore.getPlaceDetail(),
                popupStore.getBannerImgUrl(),
                popupStore.getOrganizer());
    }
}
