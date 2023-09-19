package com.bonestew.popmate.admin.presentation.dto;

import com.bonestew.popmate.admin.domain.BackOfficePopupStore;
import java.util.List;

public record BackOfficePopupStoresResponse(
    List<BackOfficePopupStoreResponse> backOfficePopupStoreResponse
) {
    public static BackOfficePopupStoresResponse from(
        List<BackOfficePopupStore> backOfficePopupStoreResponse
    ){
        List<BackOfficePopupStoreResponse> response = backOfficePopupStoreResponse.stream()
            .map(BackOfficePopupStoreResponse::from)
            .toList();
        return new BackOfficePopupStoresResponse(
            response
        );
    }
}
