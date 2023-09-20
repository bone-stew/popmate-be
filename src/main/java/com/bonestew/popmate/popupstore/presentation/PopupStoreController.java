package com.bonestew.popmate.popupstore.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.config.FolderType;
import com.bonestew.popmate.popupstore.config.service.FileService;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreCreateRequest;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfo;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreInfoResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreUpdateRequest;
import com.bonestew.popmate.popupstore.presentation.dto.StoreRelatedImages;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.bonestew.popmate.popupstore.domain.Banner;
import com.bonestew.popmate.popupstore.persistence.dto.PopupStoreDetailDto;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreHomeResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreDetailResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoresResponse;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")
@Slf4j
public class PopupStoreController {

    private final PopupStoreService popupStoreService;
    private final FileService awsFileService;

    @GetMapping
    public ApiResponse<PopupStoresResponse> getPopupStoreList(
            @RequestParam(value = "isOpeningSoon", required = false) Boolean isOpeningSoon,
            @RequestParam(value = "startDate", required = false) String startDateText,
            @RequestParam(value = "endDate", required = false) String endDateText,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "offSetRows", required = false) Integer offSetRows,
            @RequestParam(value = "rowsToGet", required = false) Integer rowsToGet
    ) {
        List<PopupStore> popupStoreList = popupStoreService.getPopupStores(isOpeningSoon,
                                                                           startDateText,
                                                                           endDateText,
                                                                           keyword,
                                                                           offSetRows,
                                                                           rowsToGet);
        return ApiResponse.success(PopupStoresResponse.from(popupStoreList));
    }

    @GetMapping("/home")
    public ApiResponse<PopupStoreHomeResponse> getHomePageContent(@AuthenticationPrincipal PopmateUser popmateUser) {
        Long userId;
        if (popmateUser == null) {
            userId = null;
        } else {
            userId = popmateUser.getUserId();
        }
        List<Banner> bannerList = popupStoreService.getBanners();
        List<PopupStore> popupStoresVisitedByList = popupStoreService.getPopupStoresVisitedBy(userId);
        List<PopupStore> popupStoresRecommendList = popupStoreService.getPopupStoresRecommend();
        List<PopupStore> popupStoresEndingSoonList = popupStoreService.getPopupStoresEndingSoon();
        return ApiResponse.success(PopupStoreHomeResponse.of(bannerList, popupStoresVisitedByList, popupStoresRecommendList,
                                                             popupStoresEndingSoonList));
    }

    @GetMapping("/{popupStoreId}")
    public ApiResponse<PopupStoreDetailResponse> getPopupStoreInfo(@PathVariable("popupStoreId") Long popupStoreId,
            @AuthenticationPrincipal PopmateUser popmateUser) {

        Long userId;
        if (popmateUser == null) {
            userId = null;
        } else {
            userId = popmateUser.getUserId();
        }
        List<PopupStoreDetailDto> popupStoreDetailDtoList = popupStoreService.getPopupStoreDetail(popupStoreId, userId);
        List<PopupStore> popupStoreNearByList = popupStoreService.getPopupStoresInDepartment(popupStoreId);
        return ApiResponse.success(
                PopupStoreDetailResponse.of(popupStoreDetailDtoList, popupStoreNearByList)
        );
    }

//    @PostMapping("/banner")
//    public ApiResponse<List<String>> addBanner(@RequestParam List<MultipartFile> multipartFiles) {
//        List<String> bannerList = new ArrayList<>();
//        for(MultipartFile file: multipartFiles){
//            Optional<String> bannerImgUrl = awsFileService.upload(file, FolderType.BANNERS);
//            bannerList.add(bannerImgUrl.get());
//        }
//        return ApiResponse.success(bannerList);
//    }


    @PostMapping(value = "/{storeId}/images", consumes = {"multipart/form-data"})
    public ApiResponse<StoreRelatedImages> addStoreImage(
            @RequestPart(value = "storeImageFiles", required = false) List<MultipartFile> storeImageFiles,
            @RequestPart(value = "storeItemImageFiles", required = false) List<MultipartFile> storeItemImageFiles) {
        StoreRelatedImages storeRelatedImages = new StoreRelatedImages();
//        log.info("storeImageFiles {}", storeImageFiles.toString() );
//        log.info("storeItemImageFiles {}",storeItemImageFiles.toString() );
        if (storeImageFiles != null) {
            List<String> storeImageList = awsFileService.uploadFiles(storeImageFiles, FolderType.STORES);
            storeRelatedImages.setPopupStoreImageList(storeImageList);
        }
        if (storeItemImageFiles != null) {
            List<String> storeItemImageList = awsFileService.uploadFiles(storeItemImageFiles, FolderType.ITEMS);
            storeRelatedImages.setPopupStoreItemImageList(storeItemImageList);
        }

//        Optional<String> bannerImgUrl = awsFileService.upload(multipartFile, FolderType.BANNERS);
//        if (bannerImgUrl.isPresent()) {
//            return ApiResponse.success(bannerImgUrl.get());
//        }
//        return ApiResponse.failure(ResultCode.FAILURE, "파일 업로드 에러");
        return ApiResponse.success(storeRelatedImages);
    }

//    @DeleteMapping("/image")
//    public ApiResponse<String> deleteStoreImage(@RequestParam MultipartFile multipartFile) {
//        Optional<String> bannerImgUrl = awsFileService.upload(multipartFile, FolderType.BANNERS);
//        if (bannerImgUrl.isPresent()) {
//            return ApiResponse.success(bannerImgUrl.get());
//        }
//        return ApiResponse.failure(ResultCode.FAILURE, "파일 업로드 에러");
//    }


    @PostMapping(value = "/new", consumes = {"multipart/form-data"})
    public ApiResponse<Long> createPopupStore(@RequestPart(value = "storeInfo") PopupStoreCreateRequest popupStoreCreateRequest,
            @RequestPart("storeImageFiles") List<MultipartFile> storeImageFiles,
            @RequestPart(value = "storeItemImageFiles", required = false) List<MultipartFile> storeItemImageFiles) {
        log.info("PopupStoreCreateRequest {}", popupStoreCreateRequest.toString());
        List<String> storeImageList = awsFileService.uploadFiles(storeImageFiles, FolderType.STORES);
        List<String> storeItemImageList = new ArrayList<>();
        if (storeItemImageFiles != null) {
            storeItemImageList = awsFileService.uploadFiles(storeItemImageFiles, FolderType.ITEMS);
        }
//        popupStoreCreateRequest.setPopupStoreImageList(storeImageList);
//        popupStoreCreateRequest.setPopupStoreItemImageList(storeItemImageList);
        Long storeId = popupStoreService.postNewPopupStore(popupStoreCreateRequest, storeImageList, storeItemImageList);
        return ApiResponse.success(storeId);
    }

    @GetMapping("/{popupStoreId}/edit")
    public ApiResponse<PopupStoreInfoResponse> getPopupStoreInfoForAdmin(@PathVariable("popupStoreId") Long popupStoreId) {
        List<PopupStoreInfo> popupStoreInfoList = popupStoreService.getPopupStoreDetailForAdmin(popupStoreId);
        return ApiResponse.success(PopupStoreInfoResponse.from(popupStoreInfoList));
    }

    @PutMapping("/{popupStoreId}")
    public ApiResponse<String> updatePopupStore(@RequestBody PopupStoreUpdateRequest popupStoreUpdateRequest) {
        log.info(popupStoreUpdateRequest.toString());
        popupStoreService.updatePopupStore(popupStoreUpdateRequest);
        return ApiResponse.success("success");
    }


}
