package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.util.List;

public record PopupStoreHomeResponse(
        List<BannerImgResponse> banners,
        List<PopupStoreVisitedResponse> popupStoresVisitedBy,
        List<PopupStoreListItemResponse> popupStoresRecommend,
        List<PopupStoreListItemResponse> popupStoresEndingSoon
) {

    public static PopupStoreHomeResponse of(List<Banner> bannerList,
                                              List<PopupStore> popupStoresVisitedByList,
                                              List<PopupStore> popupStoresRecommendList,
                                              List<PopupStore> popupStoresEndingSoonList) {
        List<BannerImgResponse> bannerImgResponses = bannerList.stream().map(BannerImgResponse::from).toList();
        List<PopupStoreVisitedResponse> popupStoresVisitedByResponses = popupStoresVisitedByList.stream()
                .map(PopupStoreVisitedResponse::from).toList();
        List<PopupStoreListItemResponse> popupStoresRecommendResponses = popupStoresRecommendList.stream().map(PopupStoreListItemResponse::from)
                .toList();
        List<PopupStoreListItemResponse> popupStoresEndingSoonResponses = popupStoresEndingSoonList.stream().map(PopupStoreListItemResponse::from)
                .toList();

        return new PopupStoreHomeResponse(
                bannerImgResponses,
                popupStoresVisitedByResponses,
                popupStoresRecommendResponses,
                popupStoresEndingSoonResponses
        );
    }

}
