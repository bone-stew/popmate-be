package com.bonestew.popmate.reservation.domain;

import com.bonestew.popmate.reservation.exception.EntryNotAllowedException;
import com.bonestew.popmate.reservation.exception.ExpiredReservationException;
import com.bonestew.popmate.reservation.exception.InvalidReservationStatusException;
import com.bonestew.popmate.user.domain.User;
import java.time.LocalDate;
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

    public UserReservation(User user,
                           Reservation reservation,
                           int guestCount,
                           String qrImgUrl,
                           UserReservationStatus status) {
        this.user = user;
        this.reservation = reservation;
        this.guestCount = guestCount;
        this.qrImgUrl = qrImgUrl;
        this.status = status;
    }

    public static UserReservation of(User user, Reservation reservation, String qrImgUrl, int guestCount) {
        return new UserReservation(user, reservation, guestCount, qrImgUrl, UserReservationStatus.RESERVED);
    }

    /**
     * 오늘 예약한 예약자인지 확인하는 메서드
     */
    public boolean isToday() {
        LocalDate reservationDate = this.reservation.getCreatedAt().toLocalDate();
        LocalDate today = LocalDateTime.now().toLocalDate();
        return reservationDate.isEqual(today);
    }

    /**
     * 입장 시간 이후인지 확인하는 메서드
     */
    public boolean isAfterNow() {
        return this.getReservation().getStartTime().isAfter(LocalDateTime.now());
    }

    /**
     * 입장 처리를 할 수 있는지 확인하는 메서드
     */
    public void validateEntry() {
        // 예약 상태가 RESERVED인 경우에만 입장 처리를 할 수 있다.
        if (this.getStatus() != UserReservationStatus.RESERVED) {
            throw new InvalidReservationStatusException(this.getUserReservationId());
        }

        // 오늘 예약한 예약자만 입장 처리를 할 수 있다.
        if (!this.isToday()) {
            throw new ExpiredReservationException(this.getUserReservationId(), this.getReservation().getCreatedAt());
        }

        // 입장 시간 이후에 입장 처리를 할 수 있다.
        if (this.isAfterNow()) {
            throw new EntryNotAllowedException(this.getReservation().getVisitStartTime(),
                this.getReservation().getVisitEndTime());
        }
    }
}
