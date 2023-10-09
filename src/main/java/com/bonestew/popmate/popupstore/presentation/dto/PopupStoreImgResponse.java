package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStoreImg;

public record PopupStoreImgResponse(
        Long popupStoreImgId,
        String imgUrl
) {

    public static PopupStoreImgResponse from(
            PopupStoreImg popupStoreImg
    ) {
        return new PopupStoreImgResponse(
                popupStoreImg.getPopupStoreImgId(),
                popupStoreImg.getImgUrl()
        );
    }
}
