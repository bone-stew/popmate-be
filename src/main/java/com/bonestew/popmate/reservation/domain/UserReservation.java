package com.bonestew.popmate.reservation.domain;

import com.bonestew.popmate.date.BaseTime;
import com.bonestew.popmate.user.domain.User;
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
public class UserReservation extends BaseTime {

    private Long userReservationId;
    private User user;
    private Reservation reservation;
    private int guestCount;
    private String qrImgUrl;
    private UserReservationStatus status;
}
