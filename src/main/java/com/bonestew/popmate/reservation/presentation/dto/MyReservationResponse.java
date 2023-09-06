package com.bonestew.popmate.reservation.presentation.dto;

import com.bonestew.popmate.reservation.domain.UserReservation;
import java.time.LocalDateTime;

public record MyReservationResponse(
    String popupStoreTitle,
    String popupStoreImageUrl,
    String popupStorePlaceDetail,
    String reservationQrImageUrl,
    int guestCount,
    LocalDateTime visitStartTime,
    LocalDateTime visitEndTime,
    String reservationStatus

) {

    public static MyReservationResponse from(UserReservation userReservation) {
        return new MyReservationResponse(
            userReservation.getReservation().getPopupStore().getTitle(),
            userReservation.getReservation().getPopupStore().getBannerImgUrl(),
            userReservation.getReservation().getPopupStore().getPlaceDetail(),
            userReservation.getQrImgUrl(),
            userReservation.getGuestCount(),
            userReservation.getReservation().getVisitStartTime(),
            userReservation.getReservation().getVisitEndTime(),
            userReservation.getReservation().getStatus().name()
        );
    }
}
