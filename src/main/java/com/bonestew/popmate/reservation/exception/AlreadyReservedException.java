package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class AlreadyReservedException extends BadRequestException {

    public AlreadyReservedException(Long userId, Long reservationId) {
        super("Already reserved. userId: " + userId + ", reservationId: " + reservationId);
    }
}
