package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.util.List;

public record MyReservationsResponse(
    List<ReservationResponse> before,
    List<ReservationResponse> after,
    List<ReservationResponse> canceled
) {

    public static MyReservationsResponse from(List<UserReservation> reservations) {
        List<ReservationResponse> before = filterReservations(reservations, UserReservationStatus.RESERVED);
        List<ReservationResponse> after = filterReservations(reservations, UserReservationStatus.VISITED);
        List<ReservationResponse> canceled = filterReservations(reservations, UserReservationStatus.CANCELED);
        return new MyReservationsResponse(before, after, canceled);
    }

    private static List<ReservationResponse> filterReservations(List<UserReservation> reservations, UserReservationStatus status) {
        return reservations.stream()
            .filter(userReservation -> userReservation.getStatus() == status)
            .map(ReservationResponse::from)
            .toList();
    }
}
