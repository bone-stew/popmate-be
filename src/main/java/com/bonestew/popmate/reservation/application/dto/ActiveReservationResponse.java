package com.bonestew.popmate.reservation.application.dto;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDateTime;

public record ActiveReservationResponse(
    LocalDateTime startTime,
    LocalDateTime endTime,
    int status,
    String popupStoreTitle,
    String description,
    LocalDateTime popupStoreOpenTime,
    LocalDateTime popupStoreCloseTime
) {

    public static ActiveReservationResponse from(Reservation reservation) {
        return new ActiveReservationResponse(
            reservation.getStartTime(),
            reservation.getEndTime(),
            reservation.getStatus(),
            reservation.getPopupStore().getTitle(),
            reservation.getPopupStore().getDescription(),
            reservation.getPopupStore().getOpenTime(),
            reservation.getPopupStore().getCloseTime()
        );
    }
}
