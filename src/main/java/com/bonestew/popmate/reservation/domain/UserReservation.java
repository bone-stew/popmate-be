package com.bonestew.popmate.reservation.domain;

import com.bonestew.popmate.user.domain.User;
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

    public UserReservation(User user, Reservation reservation, int guestCount, String qrImgUrl, UserReservationStatus status) {
        this.user = user;
        this.reservation = reservation;
        this.guestCount = guestCount;
        this.qrImgUrl = qrImgUrl;
        this.status = status;
    }

    public static UserReservation of(User user, Reservation reservation, String qrImgUrl, int guestCount) {
        return new UserReservation(user, reservation, guestCount, qrImgUrl, UserReservationStatus.RESERVED);
    }
}
