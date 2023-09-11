package com.bonestew.popmate.order.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class StockNotFoundException extends NotFoundException {

    public StockNotFoundException(Long storeId, Long itemId) {
        super("Popup store not found: " + storeId + " or Item Id not found : " + itemId);
    }
}
