package com.bonestew.popmate.popupstore.exception;

import com.bonestew.popmate.exception.PopMateException;

public class ImageUploadFailedException extends PopMateException {

    public ImageUploadFailedException() {
        super("Image Upload to AWS failed");
    }
}
