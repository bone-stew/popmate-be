package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.ReservationException;

public class InvalidReservationCancellationException extends ReservationException {

    public InvalidReservationCancellationException(Long userReservationId) {
        super("예약 취소가 유효하지 않습니다. userReservationId: " + userReservationId);
    }
}
