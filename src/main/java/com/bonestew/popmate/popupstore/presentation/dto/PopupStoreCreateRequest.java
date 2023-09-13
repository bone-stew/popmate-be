package com.bonestew.popmate.popupstore.presentation.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.popupstore.domain.PopupStoreSns;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PopupStoreCreateRequest {
    private PopupStore popupStore;
    private List<PopupStoreSns> popupStoreSnsList;
    private List<String> popupStoreImageList;
    private List<String> popupStoreItemImageList;
}
