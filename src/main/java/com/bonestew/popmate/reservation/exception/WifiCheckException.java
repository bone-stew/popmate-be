package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class WifiCheckException extends BadRequestException {

    public WifiCheckException() {
        super("Wifi check failed.");
    }
}
