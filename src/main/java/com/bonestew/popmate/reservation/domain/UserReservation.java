package com.bonestew.popmate.reservation.domain;

import com.bonestew.popmate.auth.domain.User;
import java.time.LocalDateTime;
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

    private Long userReservationId;
    private User user;
    private Reservation reservation;
    private int guestCount;
    private String qrImgUrl;
    private UserReservationStatus status;
    private LocalDateTime createdAt;
}
