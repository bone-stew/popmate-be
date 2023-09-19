package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDateTime;
import java.util.List;

public record TodayReservationResponse(
    Long popupStoreId,
    String popupStoreName,
    LocalDateTime currentReservationStartTime,
    LocalDateTime currentReservationEndTime,
    int reservedGuestCount,
    int entryGuestCount,
    List<UpComingReservationResponse> upComingReservations
) {

    public static TodayReservationResponse of(Reservation currentReservation, List<Reservation> reservations) {
        return new TodayReservationResponse(
            currentReservation.getPopupStore().getPopupStoreId(),
            currentReservation.getPopupStore().getTitle(),
            currentReservation.getVisitStartTime(),
            currentReservation.getVisitEndTime(),
            currentReservation.getCurrentGuestCount(),
            currentReservation.getEnteredGuestCount(),
            reservations.stream()
                .map(UpComingReservationResponse::from)
                .toList());
    }
}
