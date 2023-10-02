package com.bonestew.popmate.popupstore.persistence.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PopupStoreImageDeleteRequest {
    private Long popupStoreId;
    private List<String> storeImagesNotToDelete;
}
