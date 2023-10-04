package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.time.LocalDateTime;
import java.util.List;

public record PopupStoreListItemResponse(
        Long popupStoreId,
        String title,
        String categoryName,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        String placeDetail,
        String bannerImgUrl,
        String organizer
) {


    public static PopupStoreListItemResponse from(PopupStore popupStore) {
        return new PopupStoreListItemResponse(
                popupStore.getPopupStoreId(),
                popupStore.getTitle(),
                popupStore.getCategory().getCategoryName().getName(),
                popupStore.getOpenDate(),
                popupStore.getCloseDate(),
                popupStore.getPlaceDetail(),
                popupStore.getBannerImgUrl(),
                popupStore.getOrganizer());
    }
}
