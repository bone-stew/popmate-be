package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Banner;

public record BannerImgResponse(
        Long bannerId,
        String imgUrl
) {

    public static BannerImgResponse from(Banner banner) {
        return new BannerImgResponse(
                banner.getBannerId(),
                banner.getImgUrl());
    }
}
