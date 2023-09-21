package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PopupStoreInfo {
    private PopupStore popupStore;
    private PopupStoreSns popupStoreSns;
    private PopupStoreImg popupStoreImg;
    private PopupStoreItem popupStoreItem;
}
