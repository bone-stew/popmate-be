package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.UserReservation;
import java.util.List;

public record MyReservationsResponse(
    List<ReservationResponse> before,
    List<ReservationResponse> after,
    List<ReservationResponse> canceled
) {

    public static MyReservationsResponse from(List<UserReservation> reservations) {
        List<ReservationResponse> before = filterReservations(reservations, 0);
        List<ReservationResponse> after = filterReservations(reservations, 1);
        List<ReservationResponse> canceled = filterReservations(reservations, -1);
        return new MyReservationsResponse(before, after, canceled);
    }

    private static List<ReservationResponse> filterReservations(List<UserReservation> reservations, int status) {
        return reservations.stream()
            .filter(userReservation -> userReservation.getStatus() == status)
            .map(ReservationResponse::from)
            .toList();
    }
}
