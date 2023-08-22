package com.bonestew.popmate.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserReservation {

    private Long tbUserReservationId;
    private Long userId;
    private Long ReservationId;
    private int status;
    private int guestCount;
    private String QrImg;
}
