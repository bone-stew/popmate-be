package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.ReservationException;

public class ReservationFullException extends ReservationException {

    public ReservationFullException() {
        super("Reservation is full.");
    }
}
