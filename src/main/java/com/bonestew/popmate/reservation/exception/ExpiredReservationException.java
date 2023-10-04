package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;
import java.time.LocalDateTime;

public class ExpiredReservationException extends BadRequestException {

    public ExpiredReservationException(Long userReservationId, LocalDateTime createdAt) {
        super("예약이 만료되었습니다. userReservationId: " + userReservationId + ", createdAt: " + createdAt);
    }
}
