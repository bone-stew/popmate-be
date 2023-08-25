package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import java.util.List;

public record PopupStoreItemsResponse(
        String popupStoreName,
        List<PopupStoreItemResponse> popupStoreItems
) {

    public static PopupStoreItemsResponse from(
            PopupStore popupStore,
            List<PopupStoreItem> popupStoreItemList
    ) {
        List<PopupStoreItemResponse> popupStoreItemResponses = popupStoreItemList.stream().map(PopupStoreItemResponse::from)
                .toList();
        return new PopupStoreItemsResponse(
                popupStore.getTitle(),
                popupStoreItemResponses
        );
    }
}
