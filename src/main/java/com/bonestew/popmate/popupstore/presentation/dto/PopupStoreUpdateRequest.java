package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PopupStoreUpdateRequest {
    private PopupStore popupStore;
    private List<String> storeImageList;
    private List<PopupStoreSns> popupStoreSnsList;
    private List<PopupStoreItem> popupStoreItemList;
}
