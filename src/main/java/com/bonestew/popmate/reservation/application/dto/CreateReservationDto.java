package com.bonestew.popmate.reservation.application.dto;

import java.time.LocalDateTime;

public record CreateReservationDto(
    int reservationInterval,
    LocalDateTime openDateTime,
    LocalDateTime closeDateTime,
    Long popupStoreId,
    int guestLimit,
    int teamSizeLimit
) {

}
