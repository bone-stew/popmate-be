package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.ReservationException;

public class ReservationCapacityExceededException extends ReservationException {

    public ReservationCapacityExceededException(int guestCount) {
        super("Reservation capacity exceeded. guestCount: " + guestCount);
    }
}
