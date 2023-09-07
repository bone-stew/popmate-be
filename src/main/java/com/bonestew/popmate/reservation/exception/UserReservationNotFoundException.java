package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class UserReservationNotFoundException extends NotFoundException {

    public UserReservationNotFoundException(Long userReservationId) {
        super("UserReservation not found. userReservationId: " + userReservationId);
    }
}
