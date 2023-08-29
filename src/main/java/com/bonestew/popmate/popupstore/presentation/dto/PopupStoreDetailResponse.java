package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import java.time.LocalDateTime;
import java.util.List;

public record PopupStoreDetailResponse(
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
        String departmentName,
        String departmentDescription,
        double latitude,
        double longitude,
        Long views,
        List<PopupStoreSns> popupStoreSnsResponses,
        List<PopupStoreImg> popupStoreImgResponses
) {

    public static PopupStoreDetailResponse of(PopupStoreDetailDto popupStoreDto,
                                                List<PopupStoreSns> popupStoreSnsList,
                                                List<PopupStoreImg> popupStoreImgList) {
        return new PopupStoreDetailResponse(
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
                popupStoreDto.getDepartment().getName(),
                popupStoreDto.getDepartment().getPlaceDescription(),
                popupStoreDto.getDepartment().getLatitude(),
                popupStoreDto.getDepartment().getLongitude(),
                popupStoreDto.getPopupStore().getViews(),
                popupStoreSnsList,
                popupStoreImgList
        );
    }
}
