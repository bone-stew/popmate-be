package com.bonestew.popmate.admin.presentation.dto;

import com.bonestew.popmate.admin.domain.MainBanner;
import java.time.LocalDateTime;
import java.util.List;

public record BannerResponse(
    Long bannerId,
    String imgUrl,
    String popupStoreId,
    LocalDateTime createdAt
) {
    public static BannerResponse from(
        MainBanner mainBanner
    ){
        return new BannerResponse(
            mainBanner.getBannerId(),
            mainBanner.getImgUrl(),
            mainBanner.getPopupStoreId(),
            mainBanner.getCreatedAt()
        );
    }
}
