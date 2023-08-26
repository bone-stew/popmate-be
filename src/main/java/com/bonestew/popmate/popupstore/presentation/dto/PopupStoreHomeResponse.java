package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.util.List;

public record PopupStoreHomeResponse(
        List<BannerImgResponse> banners,
        List<PopupStoreVisitedResponse> popupStoresVisitedBy,
        List<PopupStoreResponse> popupStoresRecommend,
        List<PopupStoreResponse> popupStoresEndingSoon
) {

    public static PopupStoreHomeResponse of(List<Banner> bannerList,
                                              List<PopupStore> popupStoresVisitedByList,
                                              List<PopupStore> popupStoresRecommendList,
                                              List<PopupStore> popupStoresEndingSoonList) {
        List<BannerImgResponse> bannerImgResponses = bannerList.stream().map(BannerImgResponse::from).toList();
        List<PopupStoreVisitedResponse> popupStoresVisitedByResponses = popupStoresVisitedByList.stream()
                .map(PopupStoreVisitedResponse::from).toList();
        List<PopupStoreResponse> popupStoresRecommendResponses = popupStoresRecommendList.stream().map(PopupStoreResponse::from)
                .toList();
        List<PopupStoreResponse> popupStoresEndingSoonResponses = popupStoresEndingSoonList.stream().map(PopupStoreResponse::from)
                .toList();

        return new PopupStoreHomeResponse(
                bannerImgResponses,
                popupStoresVisitedByResponses,
                popupStoresRecommendResponses,
                popupStoresEndingSoonResponses
        );
    }

}
