package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record PopupStoreDetailResponse(
        Long popupStoreId,
        String title,
        String categoryName,
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
        Boolean reservationEnabled,
        Department department,
        List<PopupStoreSnsResponse> popupStoreSnsResponses,
        List<PopupStoreImgResponse> popupStoreImgResponses,
        List<PopupStoreResponse> popupStoresNearBy
) {

    public static PopupStoreDetailResponse of(List<PopupStoreDetailDto> popupStoreDtoList,
                                              List<PopupStore> popupStoresNearBy) {
        PopupStore popupStore = popupStoreDtoList.get(0).getPopupStore();
        List<PopupStoreSnsResponse> popupStoreSnsResponse = new ArrayList<>();
        List<PopupStoreImgResponse> popupStoreImgResponse = new ArrayList<>();
        HashSet<Long> popupStoreSnsIds = new HashSet<>();
        HashSet<Long> popupStoreImgIds = new HashSet<>();
        for (PopupStoreDetailDto popupStoreDetailDto : popupStoreDtoList) {
            if (popupStoreDetailDto.getPopupStoreSns() != null) {
                PopupStoreSns sns = popupStoreDetailDto.getPopupStoreSns();
                Long snsId = sns.getSnsId();
                if (!popupStoreSnsIds.contains(snsId)) {
                    popupStoreSnsIds.add(snsId);
                    popupStoreSnsResponse.add(PopupStoreSnsResponse.from(sns));
                }
            }
            if (popupStoreDetailDto.getPopupStoreImg() != null) {
                PopupStoreImg img = popupStoreDetailDto.getPopupStoreImg();
                Long imgId = img.getPopupStoreImgId();
                if (!popupStoreImgIds.contains(imgId)) {
                    popupStoreImgIds.add(imgId);
                    popupStoreImgResponse.add(PopupStoreImgResponse.from(img));
                }
            }
        }
        List<PopupStoreResponse> popupStoresNearByResponses = popupStoresNearBy.stream().map(PopupStoreResponse::from).toList();
        return new PopupStoreDetailResponse(
                popupStore.getPopupStoreId(),
                popupStore.getTitle(),
                popupStore.getCategory().getCategoryName().getName(),
                popupStore.getOrganizer(),
                popupStore.getPlaceDetail(),
                popupStore.getDescription(),
                popupStore.getEventDescription(),
                popupStore.getBannerImgUrl(),
                popupStore.getEntryFee(),
                popupStore.getOpenDate(),
                popupStore.getCloseDate(),
                popupStore.getOpenTime(),
                popupStore.getCloseTime(),
                popupStoreDtoList.get(0).getUserReservationStatus().getCode(),
                popupStore.getViews(),
                popupStore.getReservationEnabled(),
                popupStore.getDepartment(),
                popupStoreSnsResponse,
                popupStoreImgResponse,
                popupStoresNearByResponses
        );
    }
}
