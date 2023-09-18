package com.bonestew.popmate.admin.presentation;

import com.bonestew.popmate.admin.application.AdminService;
import com.bonestew.popmate.admin.domain.MainBanner;
import com.bonestew.popmate.admin.presentation.dto.BannersResponse;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.exception.enums.ResultCode;
import com.bonestew.popmate.popupstore.config.FolderType;
import com.bonestew.popmate.popupstore.config.service.FileService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final FileService awsFileService;

    @PostMapping("/banners/new")
    public ApiResponse<MainBanner> addMainBanner(@RequestParam MultipartFile multipartFile, @RequestParam Long popupStoreId) {
        Optional<String> bannerImgUrl = awsFileService.upload(multipartFile, FolderType.BANNERS);
        if (bannerImgUrl.isPresent()) {
            adminService.insertMainBanner(popupStoreId, String.valueOf(bannerImgUrl));
            MainBanner mainBanner = adminService.getOneMainBanner();
            return ApiResponse.success(mainBanner);
        }
        return ApiResponse.failure(ResultCode.FAILURE, "파일 업로드 에러");
    }

    @DeleteMapping("/banners/{bannerId}")
    public ApiResponse<String> deleteMainBanner(@PathVariable("bannerId") Long bannerId){
        adminService.deleteBanner(bannerId);
        return ApiResponse.success("배너 이미지를 삭제하였습니다.");
    }

    @GetMapping("/banners")
    public ApiResponse<BannersResponse> getMainBanner(){
        List<MainBanner> mainBanner = adminService.getMainBanner();
        return ApiResponse.success(
            BannersResponse.from(mainBanner)
        );
    }
}
