package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class InvalidGuestCountException extends BadRequestException {

    public InvalidGuestCountException(int guestCount) {
        super("Invalid guest count: " + guestCount);
    }
}
