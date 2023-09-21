package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.time.LocalDateTime;

public record PopupStoreResponse(
        Long popupStoreId,
        String title,
        String departmentName,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        String placeDetail,
        String bannerImgUrl,
        String organizer,
        LocalDateTime createdAt,
        int total
) {


    public static PopupStoreResponse from(PopupStore popupStore) {
        return new PopupStoreResponse(
                popupStore.getPopupStoreId(),
                popupStore.getTitle(),
                popupStore.getDepartment().getName(),
                popupStore.getOpenDate(),
                popupStore.getCloseDate(),
                popupStore.getPlaceDetail(),
                popupStore.getBannerImgUrl(),
                popupStore.getOrganizer(),
                popupStore.getCreatedAt(),
                popupStore.getTotal());
    }
}
