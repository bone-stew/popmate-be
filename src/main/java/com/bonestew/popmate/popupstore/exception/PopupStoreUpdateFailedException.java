package com.bonestew.popmate.popupstore.exception;

import com.bonestew.popmate.exception.PopMateException;

public class PopupStoreUpdateFailedException extends PopMateException {

    public PopupStoreUpdateFailedException(Long popupStoreId) {
        super("Popup update failed: " + popupStoreId);
    }
}
