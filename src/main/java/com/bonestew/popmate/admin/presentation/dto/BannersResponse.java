package com.bonestew.popmate.admin.presentation.dto;

import com.bonestew.popmate.admin.domain.MainBanner;
import java.util.List;

public record BannersResponse(
    List<BannerResponse> bannerResponses
) {
    public static BannersResponse from(
        List<MainBanner> mainBanner
    ) {
        List<BannerResponse> response = mainBanner.stream()
            .map(BannerResponse::from)
            .toList();
        return new BannersResponse(
            response
        );
    }
}
