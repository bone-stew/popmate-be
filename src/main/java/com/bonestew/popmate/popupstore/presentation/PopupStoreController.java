package com.bonestew.popmate.popupstore.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreHomeResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreDetailResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreItemsResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoresResponse;
import com.bonestew.popmate.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")
public class PopupStoreController {

    private final PopupStoreService popupStoreService;

    @GetMapping
    public ApiResponse<PopupStoresResponse> getPopupStoreList(
            @RequestBody(required = false) PopupStoreSearchRequest popupStoreSearchRequest) {
        List<PopupStore> popupStoreList = popupStoreService.getPopupStores(popupStoreSearchRequest);
        return ApiResponse.success(PopupStoresResponse.from(popupStoreList));
    }

    @GetMapping("/home")
    public ApiResponse<PopupStoreHomeResponse> getHomePageContent(@RequestBody User user) { //Oauth 적용후 유저 정보 가져오기
        List<Banner> bannerList = popupStoreService.getBanners();
        List<PopupStore> popupStoresVisitedByList = popupStoreService.getPopupStoresVisitedBy(user);
        List<PopupStore> popupStoresRecommendList = popupStoreService.getPopupStoresRecommend();
        List<PopupStore> popupStoresEndingSoonList = popupStoreService.getPopupStoresEndingSoon();
        return ApiResponse.success(PopupStoreHomeResponse.from(bannerList, popupStoresVisitedByList, popupStoresRecommendList,
                popupStoresEndingSoonList));
    }

    @GetMapping("/{popupStoreId}")
    public ApiResponse<PopupStoreDetailResponse> getPopupStoreInfo(@PathVariable("popupStoreId") Long popupStoreId,
                                                                   @RequestBody User user) { //Oauth 적용후 유저 정보 가져오기
        PopupStoreDetailDto popupStoreDto = popupStoreService.getPopupStoreDetail(popupStoreId, user);
        List<PopupStoreSns> popupStoreSnsList = popupStoreService.getPopupStoreSnss(popupStoreId);
        List<PopupStoreImg> popupStoreImgList = popupStoreService.getPopupStoreImgs(popupStoreId);
        return ApiResponse.success(PopupStoreDetailResponse.from(popupStoreDto, popupStoreSnsList, popupStoreImgList));
    }

    @GetMapping("/{popupStoreId}/goods")
    public ApiResponse<PopupStoreItemsResponse> getPopupStoreItems(@PathVariable("popupStoreId") Long popupStoreId) {
        PopupStore popupStore = popupStoreService.getPopupStore(popupStoreId);
        List<PopupStoreItem> popupStoreItemList = popupStoreService.getPopupStoreGoods(popupStoreId);
        return ApiResponse.success(PopupStoreItemsResponse.from(popupStore, popupStoreItemList));
    }
}