package com.bonestew.popmate.admin.presentation.dto;

import com.bonestew.popmate.admin.domain.BackOfficePopupStore;

public record BackOfficePopupStoreResponse(
    Long popupStoreId,
    String title
) {
    public static BackOfficePopupStoreResponse from(
        BackOfficePopupStore backOfficePopupStore
    ){
        return new BackOfficePopupStoreResponse(
            backOfficePopupStore.getPopupStoreId(),
            backOfficePopupStore.getTitle()
        );
    }
}
