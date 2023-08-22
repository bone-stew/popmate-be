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
public class PopupStoreItem {

    private Long tbItemId;
    private Long storeId;
    private String name;
    private int price;
    private String imgUrl;
    private int stock;
    private int orderLimit;
}
