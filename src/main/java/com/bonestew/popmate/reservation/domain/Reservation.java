package com.bonestew.popmate.reservation.domain;

import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.reservation.exception.ReservationCapacityExceededException;
import com.bonestew.popmate.reservation.exception.ReservationFullException;
import com.bonestew.popmate.reservation.exception.ReservationNotActiveException;
import com.bonestew.popmate.reservation.exception.TeamCapacityExceededException;
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
public class Reservation {

    private Long reservationId;
    private PopupStore popupStore;
    private int guestLimit; // 총 예약 가능 인원
    private int teamSizeLimit; // 팀 당 예약 가능 인원
    private int currentGuestCount; // 현재 예약 인원
    private ReservationStatus status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime visitStartTime;
    private LocalDateTime visitEndTime;
    private LocalDateTime createdAt;

    public void validate(final int guestCount) {
        if (this.status != ReservationStatus.ACTIVE) {
            throw new ReservationNotActiveException(this.getReservationId());
        }
        if (this.currentGuestCount >= this.guestLimit) {
            throw new ReservationFullException();
        }
        if (guestCount > this.getTeamSizeLimit()) {
            throw new TeamCapacityExceededException(guestCount);
        }
        if (this.currentGuestCount + guestCount > this.guestLimit) {
            throw new ReservationCapacityExceededException(guestCount);
        }
    }

    public void increaseCurrentGuestCount(int guestCount) {
        this.currentGuestCount += guestCount;
    }
}
