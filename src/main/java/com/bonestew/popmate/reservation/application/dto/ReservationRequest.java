package com.bonestew.popmate.reservation.application.dto;

public record ReservationRequest(
    int guestCount,
    double latitude,
    double longitude
) {

}
