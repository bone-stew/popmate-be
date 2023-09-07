package com.bonestew.popmate.popupstore.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.config.FolderType;
import com.bonestew.popmate.popupstore.config.service.AwsFileService;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreHomeResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreDetailResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreSearchRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoresResponse;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")
public class PopupStoreController {

    private final PopupStoreService popupStoreService;
    private final AwsFileService awsFileService;

    @GetMapping
    public ApiResponse<PopupStoresResponse> getPopupStoreList(
            @RequestBody(required = false) PopupStoreSearchRequest popupStoreSearchRequest) {
        List<PopupStore> popupStoreList = popupStoreService.getPopupStores(popupStoreSearchRequest);
        return ApiResponse.success(PopupStoresResponse.from(popupStoreList));
    }

    @GetMapping("/home")
    public ApiResponse<PopupStoreHomeResponse> getHomePageContent(@RequestBody Long userId) { //Oauth 적용후 유저 정보 가져오기
        List<Banner> bannerList = popupStoreService.getBanners();
        List<PopupStore> popupStoresVisitedByList = popupStoreService.getPopupStoresVisitedBy(userId);
        List<PopupStore> popupStoresRecommendList = popupStoreService.getPopupStoresRecommend();
        List<PopupStore> popupStoresEndingSoonList = popupStoreService.getPopupStoresEndingSoon();
        return ApiResponse.success(PopupStoreHomeResponse.of(bannerList, popupStoresVisitedByList, popupStoresRecommendList,
                                                             popupStoresEndingSoonList));
    }

    @GetMapping("/{popupStoreId}")
    public ApiResponse<PopupStoreDetailResponse> getPopupStoreInfo(@PathVariable("popupStoreId") Long popupStoreId,
            @RequestBody Long userId) { //Oauth 적용후 유저 정보 가져오기
        PopupStoreDetailDto popupStoreDto = popupStoreService.getPopupStoreDetail(popupStoreId, userId);
        List<PopupStoreSns> popupStoreSnsList = popupStoreService.getPopupStoreSnss(popupStoreId);
        List<PopupStoreImg> popupStoreImgList = popupStoreService.getPopupStoreImgs(popupStoreId);
        return ApiResponse.success(PopupStoreDetailResponse.of(popupStoreDto, popupStoreSnsList, popupStoreImgList));
    }

//    @GetMapping("/{popupStoreId}/items")
//    public ApiResponse<PopupStoreItemsResponse> getPopupStoreItems(@PathVariable("popupStoreId") Long popupStoreId) {
//        PopupStore popupStore = popupStoreService.getPopupStore(popupStoreId);
//        List<PopupStoreItem> popupStoreItemList = popupStoreService.getPopupStoreGoods(popupStoreId);
//        return ApiResponse.success(PopupStoreItemsResponse.of(popupStore, popupStoreItemList));
//    }

    @PostMapping("/banner")
    public ApiResponse<String> addBanner(@RequestParam MultipartFile multipartFile) {
        Optional<String> bannerImgUrl = awsFileService.upload(multipartFile, FolderType.BANNERS);
        if (bannerImgUrl.isPresent()) {
            return ApiResponse.success(bannerImgUrl.get());
        }
        return ApiResponse.failure(ResultCode.FAILURE, "파일 업로드 에러");
    }
}

