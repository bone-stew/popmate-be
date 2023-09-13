package com.bonestew.popmate.order.presentation.dto;

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
public class StockCheckRequest {
    private Long itemId;
    private String name;
    private Long popupStoreId;
    private String imgUrl;
    private int amount;
    private int stock;
    private int orderLimit;
    private int totalQuantity;
}
