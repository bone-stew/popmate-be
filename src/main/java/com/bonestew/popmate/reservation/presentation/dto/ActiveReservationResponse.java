package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDateTime;

public record ActiveReservationResponse(
    Long reservationId,
    LocalDateTime startTime,
    LocalDateTime endTime,
    String status,
    String popupStoreTitle,
    String popupStoreDescription,
    LocalDateTime popupStoreOpenTime,
    LocalDateTime popupStoreCloseTime
) {

    public static ActiveReservationResponse from(Reservation reservation) {
        return new ActiveReservationResponse(
            reservation.getReservationId(),
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getStatus().getDescription(),
            reservation.getPopupStore().getTitle(),
            reservation.getPopupStore().getDescription(),
            reservation.getPopupStore().getOpenTime(),
            reservation.getPopupStore().getCloseTime()
        );
    }
}
