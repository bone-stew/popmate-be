package com.bonestew.popmate.popupstore.persistence.dto;

import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PopupStoreItemUpdateDto {
    private Long popupStoreId;
    private PopupStoreItem popupStoreItem;
}
