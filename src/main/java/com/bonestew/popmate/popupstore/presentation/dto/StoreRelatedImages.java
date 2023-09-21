package com.bonestew.popmate.popupstore.presentation.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class StoreRelatedImages {
    List<String> popupStoreImageList;
    List<String> popupStoreItemImageList;
}
