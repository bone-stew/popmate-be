package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;

public record PopupStoreSnsResponse(
        Long snsId,
        String platform,
        String url
) {

    public static PopupStoreSnsResponse from(
            PopupStoreSns popupStoreSns
    ) {
        return new PopupStoreSnsResponse(
                popupStoreSns.getSnsId(),
                popupStoreSns.getPlatform(),
                popupStoreSns.getUrl()
        );
    }
}
