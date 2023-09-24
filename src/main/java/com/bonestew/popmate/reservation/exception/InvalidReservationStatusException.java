package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class InvalidReservationStatusException extends BadRequestException {

    public InvalidReservationStatusException(Long userReservationId) {
        super(String.format("예약 상태가 유효하지 않습니다. userReservationId: %d", userReservationId));
    }
}
