package com.bonestew.popmate.reservation.application.dto;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import java.time.LocalDateTime;

public record CreateReservationDto(
    int reservationInterval,
    LocalDateTime openDateTime,
    LocalDateTime closeDateTime,
    Long popupStoreId,
    int guestLimit,
    int teamSizeLimit
) {

    public static CreateReservationDto from(PopupStore popupStore) {
        CreateReservationDto createReservationDto = new CreateReservationDto(
            popupStore.getReservationInterval(),
            popupStore.getOpenDate(),
            popupStore.getCloseDate(),
            popupStore.getPopupStoreId(),
            popupStore.getMaxCapacity(),
            popupStore.getTeamSizeLimit()
        );
        System.out.println("createReservationDto = " + createReservationDto);
        return createReservationDto;
    }
}
