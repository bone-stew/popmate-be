package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.UserReservation;
import java.time.LocalDateTime;

public record ReservationEntranceResponse(
    Long userReservationId,
    Long userId,
    String userName,
    String email,
    int guestCount,
    String status,
    LocalDateTime reservationTime
) {

    public static ReservationEntranceResponse from(UserReservation userReservation) {
        return new ReservationEntranceResponse(
            userReservation.getUserReservationId(),
            userReservation.getUser().getUserId(),
            userReservation.getUser().getName(),
            userReservation.getUser().getEmail(),
            userReservation.getGuestCount(),
            userReservation.getStatus().getDescription(),
            userReservation.getCreatedAt()
        );
    }
}
