package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.PopMateException;

public class QrCodeGenerationException extends PopMateException {

    public QrCodeGenerationException() {
        super("An error occurred during QR code generation.");
    }
}
