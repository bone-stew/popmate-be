package com.bonestew.popmate.popupstore.domain;

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
public class PopupStoreImg {

    private Long imgId;
    private Long storeId;
    private String imgUrl;
}
