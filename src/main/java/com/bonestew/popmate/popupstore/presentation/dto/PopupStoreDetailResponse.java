package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import java.time.LocalDateTime;
import java.util.List;

public record PopupStoreDetailResponse(
        Long popupStoreId,
        String title,
        String organizer,
        String placeDetail,
        String description,
        String eventDescription,
        String bannerImgUrl,
        int entryFee,
        LocalDateTime openDate,
        LocalDateTime closeDate,
        LocalDateTime openTime,
        LocalDateTime closeTime,
        int status,
        Long views,
        Department department,
        List<PopupStoreSns> popupStoreSnsResponses,
        List<PopupStoreImg> popupStoreImgResponses,
        List<PopupStore> popupStoresNearBy
) {

    public static PopupStoreDetailResponse of(PopupStoreDetailDto popupStoreDto,
            List<PopupStoreSns> popupStoreSnsList,
            List<PopupStoreImg> popupStoreImgList,
            List<PopupStore> popupStoresNearBy) {
        return new PopupStoreDetailResponse(
                popupStoreDto.getPopupStore().getPopupStoreId(),
                popupStoreDto.getPopupStore().getTitle(),
                popupStoreDto.getPopupStore().getOrganizer(),
                popupStoreDto.getPopupStore().getPlaceDetail(),
                popupStoreDto.getPopupStore().getDescription(),
                popupStoreDto.getPopupStore().getEventDescription(),
                popupStoreDto.getPopupStore().getBannerImgUrl(),
                popupStoreDto.getPopupStore().getEntryFee(),
                popupStoreDto.getPopupStore().getOpenDate(),
                popupStoreDto.getPopupStore().getCloseDate(),
                popupStoreDto.getPopupStore().getOpenTime(),
                popupStoreDto.getPopupStore().getCloseTime(),
                popupStoreDto.getUserReservationStatus().getCode(),
                popupStoreDto.getPopupStore().getViews(),
                popupStoreDto.getDepartment(),
                popupStoreSnsList,
                popupStoreImgList,
                popupStoresNearBy
        );
    }
}
