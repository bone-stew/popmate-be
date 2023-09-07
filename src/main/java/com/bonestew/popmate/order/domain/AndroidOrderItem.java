package com.bonestew.popmate.order.domain;

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
public class AndroidOrderItem {
    private int itemId;
    private String name;
    private int popupStoreId;
    private String imgUrl;
    private int amount;
    private int stock;
    private int orderLimit;
    private int totalQuantity;
}
