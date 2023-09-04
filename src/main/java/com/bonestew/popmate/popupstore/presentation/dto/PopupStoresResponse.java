package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.util.List;

public record PopupStoresResponse(
        List<PopupStoreResponse> popupStores
) {

    public static PopupStoresResponse from(List<PopupStore> popupStores) {
        return new PopupStoresResponse(popupStores
                .stream()
                .map(PopupStoreResponse::from)
                .toList());
    }

}
