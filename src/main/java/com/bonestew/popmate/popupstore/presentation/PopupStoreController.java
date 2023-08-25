package com.bonestew.popmate.popupstore.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.config.FolderType;
import com.bonestew.popmate.popupstore.config.service.AwsFileService;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreResponse;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/{popupStoreId}")
    public ApiResponse<PopupStoreResponse> getPopupStore(@PathVariable("popupStoreId") Long popupStoreId) {
        PopupStore popupStore = popupStoreService.getPopupStore(popupStoreId);
        return ApiResponse.success(PopupStoreResponse.from(popupStore));
    }

    @PostMapping("/banner")
    public ApiResponse<String> addBanner(@RequestParam MultipartFile multipartFile) {
        Optional<String> bannerImgUrl = awsFileService.upload(multipartFile, FolderType.BANNERS);
        if (bannerImgUrl.isPresent()) {
            return ApiResponse.success(bannerImgUrl.get());
        }
        return ApiResponse.failure(ResultCode.FAILURE, "파일 업로드 에러");
    }
}
