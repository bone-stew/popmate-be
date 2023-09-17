package com.bonestew.popmate.popupstore.persistence.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreImg;
import com.bonestew.popmate.popupstore.domain.PopupStoreItem;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PopupStoreCreateDto {
    private PopupStore popupStore;
    private List<PopupStoreSns> popupStoreSnsList;
    private List<PopupStoreImg> popupStoreImageList;
    private List<PopupStoreItem> popupStoreItemList;
}
