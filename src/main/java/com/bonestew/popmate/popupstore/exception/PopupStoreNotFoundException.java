package com.bonestew.popmate.popupstore.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class PopupStoreNotFoundException extends NotFoundException {

    public PopupStoreNotFoundException(Long popupStoreId) {
        super("Popup store not found: " + popupStoreId);
    }
}
