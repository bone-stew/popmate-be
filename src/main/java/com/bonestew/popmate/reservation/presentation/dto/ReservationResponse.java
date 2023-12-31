package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.time.LocalDateTime;

public record ReservationResponse(
    Long userReservationId,
    Long reservationId,
    String reservationStatus,
    LocalDateTime startTime,
    LocalDateTime endTime,
    Long popupStoreId,
    String popupStoreTitle,
    String bannerImgUrl
) {

    public static ReservationResponse from(UserReservation userReservation) {
        return new ReservationResponse(
            userReservation.getUserReservationId(),
            userReservation.getReservation().getReservationId(),
            userReservation.getStatus().getDescription(),
            userReservation.getReservation().getStartTime(),
            userReservation.getReservation().getEndTime(),
            userReservation.getReservation().getPopupStore().getPopupStoreId(),
            userReservation.getReservation().getPopupStore().getTitle(),
            userReservation.getReservation().getPopupStore().getBannerImgUrl()
        );
    }
}
