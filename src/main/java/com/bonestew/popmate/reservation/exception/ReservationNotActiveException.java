package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class ReservationNotActiveException extends BadRequestException {

    public ReservationNotActiveException(Long reservationId) {
        super("Reservation is not active. reservationId: " + reservationId);
    }
}
