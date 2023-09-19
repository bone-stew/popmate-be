package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class PopupStoreNotInProgressException extends BadRequestException {

    public PopupStoreNotInProgressException(Long popupStoreId) {
        super("The popup store is not in progress. popupStoreId: " + popupStoreId + "");
    }
}
