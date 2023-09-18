package com.bonestew.popmate.admin.presentation.dto;

import com.bonestew.popmate.admin.domain.MainBanner;
import java.time.LocalDateTime;

public record BannerResponse(
    Long bannerId,
    Long popupStoreId,
    String title,
    String organizer,
    String placeDetail,
    String openDate,
    String closeDate,
    String bannerImgUrl,
    String imgUrl
) {
    public static BannerResponse from(
        MainBanner mainBanner
    ){
        return new BannerResponse(
            mainBanner.getBannerId(),
            mainBanner.getPopupStoreId(),
            mainBanner.getTitle(),
            mainBanner.getOrganizer(),
            mainBanner.getPlaceDetail(),
            mainBanner.getOpenDate(),
            mainBanner.getCloseDate(),
            mainBanner.getBannerImgUrl(),
            mainBanner.getImgUrl()
        );
    }
}
