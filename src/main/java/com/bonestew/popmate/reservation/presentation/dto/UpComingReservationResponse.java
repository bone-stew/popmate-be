package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDateTime;

public record UpComingReservationResponse(
    Long reservationId,
    LocalDateTime visitStartTime,
    LocalDateTime visitEndTime,
    int currentGuestCount,
    String status
) {

    public static UpComingReservationResponse from(Reservation reservation) {
        return new UpComingReservationResponse(
            reservation.getReservationId(),
            reservation.getVisitStartTime(),
            reservation.getVisitEndTime(),
            reservation.getCurrentGuestCount(),
            reservation.getStatus().getDescription());
    }
}
