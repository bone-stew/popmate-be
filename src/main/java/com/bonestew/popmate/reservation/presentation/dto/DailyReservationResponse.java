package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDateTime;

public record DailyReservationResponse(
    Long reservationId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    LocalDateTime visitStartTime,
    LocalDateTime visitEndTime,
    int guestLimit,
    int currentGuestCount,
    String status
) {

    public static DailyReservationResponse from(Reservation reservation) {
        return new DailyReservationResponse(
            reservation.getReservationId(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getVisitStartTime(),
            reservation.getVisitEndTime(),
            reservation.getGuestLimit(),
            reservation.getCurrentGuestCount(),
            reservation.getStatus().getDescription());
    }
}
