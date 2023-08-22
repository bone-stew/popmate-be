package com.bonestew.popmate.reservation.domain;


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
    private Long storeId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int guestLimit;
    private int currentGuestCount;
}
