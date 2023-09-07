package com.bonestew.popmate.order.presentation.dto;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;

public record PopupStoreItemResponse(
    Long itemId,
    String name,
    Long popupStoreId,
    String imgUrl,
    int amount,
    int stock,
    int orderLimit
    ){
    public static PopupStoreItemResponse from(
        PopupStoreItem popupStoreItem
    ){
        return new PopupStoreItemResponse(
            popupStoreItem.getPopupStoreItemId(),
            popupStoreItem.getName(),
            popupStoreItem.getPopupStore().getPopupStoreId(),
            popupStoreItem.getImgUrl(),
            popupStoreItem.getAmount(),
            popupStoreItem.getStock(),
            popupStoreItem.getOrderLimit()
        );
    }
}
