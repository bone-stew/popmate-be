package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {

    public ReservationNotFoundException(Long popupStoreId) {
        super("Reservation not found. popupStoreId: " + popupStoreId);
    }
}
