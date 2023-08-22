package com.bonestew.popmate.popupstore.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.popupstore.presentation.dto.PopupStoreResponse;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.application.PopupStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")
public class PopupStoreController {

    private final PopupStoreService popupStoreService;


    @GetMapping("/{popupStoreId}")
    public ApiResponse<PopupStoreResponse> getPopupStore(@PathVariable("popupStoreId") Long popupStoreId) {
        PopupStore popupStore = popupStoreService.getPopupStore(popupStoreId);
        return ApiResponse.success(PopupStoreResponse.from(popupStore));
    }
}
