package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.user.domain.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record PopupStoreInfoResponse(
        Long popupStoreId,
        User user,
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
        Long views,
        int maxCapacity,
        Boolean reservationEnabled,
        Boolean salesEnabled,
        int reservationInterval,
        int intervalCapacity,
        int teamSizeLimit,
        Department department,
        List<PopupStoreSns> popupStoreSnsResponse,
        List<PopupStoreImg> popupStoreImgResponse,
        List<PopupStoreItem> popupStoreItemResponse
) {

    public static PopupStoreInfoResponse from(List<PopupStoreInfo> popupStoreInfoList) {
        PopupStoreInfo popupStoreInfo = popupStoreInfoList.get(0);
        List<PopupStoreSns> popupStoreSnsResponses = new ArrayList<>();
        List<PopupStoreImg> popupStoreImgResponses = new ArrayList<>();
        List<PopupStoreItem> popupStoreItemResponses = new ArrayList<>();
        HashSet<Long> snsIdSet = new HashSet<>();
        HashSet<Long> imgIdSet = new HashSet<>();
        HashSet<Long> itemIdSet = new HashSet<>();
        for (PopupStoreInfo info : popupStoreInfoList) {
            PopupStoreSns sns = info.getPopupStoreSns();
            if (sns != null) {
                Long snsId = sns.getSnsId();
                if (!snsIdSet.contains(snsId)) {
                    popupStoreSnsResponses.add(sns);
                    snsIdSet.add(snsId);
                }
            }
            if (info.getPopupStoreImg() != null) {
                PopupStoreImg img = info.getPopupStoreImg();
                Long imgId = img.getPopupStoreImgId();
                if (!imgIdSet.contains(imgId)) {
                    popupStoreImgResponses.add(img);
                    imgIdSet.add(imgId);
                }
            }
            PopupStoreItem item = info.getPopupStoreItem();
            if (item != null) {
                Long itemId = item.getPopupStoreItemId();
                if (!itemIdSet.contains(itemId) && item.getIsOnSale() == true) {
                    popupStoreItemResponses.add(item);
                    itemIdSet.add(itemId);
                }
            }
        }
        return new PopupStoreInfoResponse(
                popupStoreInfo.getPopupStore().getPopupStoreId(),
                popupStoreInfo.getPopupStore().getUser(),
                popupStoreInfo.getPopupStore().getTitle(),
                popupStoreInfo.getPopupStore().getOrganizer(),
                popupStoreInfo.getPopupStore().getPlaceDetail(),
                popupStoreInfo.getPopupStore().getDescription(),
                popupStoreInfo.getPopupStore().getEventDescription(),
                popupStoreInfo.getPopupStore().getBannerImgUrl(),
                popupStoreInfo.getPopupStore().getEntryFee(),
                popupStoreInfo.getPopupStore().getOpenDate(),
                popupStoreInfo.getPopupStore().getCloseDate(),
                popupStoreInfo.getPopupStore().getOpenTime(),
                popupStoreInfo.getPopupStore().getCloseTime(),
                popupStoreInfo.getPopupStore().getViews(),
                popupStoreInfo.getPopupStore().getMaxCapacity(),
                popupStoreInfo.getPopupStore().getReservationEnabled(),
                popupStoreInfo.getPopupStore().getSalesEnabled(),
                popupStoreInfo.getPopupStore().getReservationInterval(),
                popupStoreInfo.getPopupStore().getIntervalCapacity(),
                popupStoreInfo.getPopupStore().getTeamSizeLimit(),
                popupStoreInfo.getPopupStore().getDepartment(),
                popupStoreSnsResponses,
                popupStoreImgResponses,
                popupStoreItemResponses
        );
    }
}
