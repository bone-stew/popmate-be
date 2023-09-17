package com.bonestew.popmate.order.presentation.dto;

import com.bonestew.popmate.order.domain.StockCheckItem;
import java.util.List;

public record StockCheckItemsResponse(
    List<StockCheckItemResponse> stockCheckItemResponse
) {
    public static StockCheckItemsResponse from(
        List<StockCheckItem> stockCheckItem
    ){
        List<StockCheckItemResponse> response = stockCheckItem.stream()
            .map(StockCheckItemResponse::from)
            .toList();
        return new StockCheckItemsResponse(
            response
        );
    }
}
