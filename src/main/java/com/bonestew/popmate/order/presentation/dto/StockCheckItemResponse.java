package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.StockCheckItem;

public record StockCheckItemResponse(
    Long itemId,
    boolean check
) {
    public static StockCheckItemResponse from(
        StockCheckItem stockCheckItem
    ){
        return new StockCheckItemResponse(
            stockCheckItem.getItemId(),
            stockCheckItem.getCheck()
        );
    }

}
