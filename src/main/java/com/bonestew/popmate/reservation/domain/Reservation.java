package com.bonestew.popmate.reservation.domain;


import com.bonestew.popmate.date.BaseTime;
import com.bonestew.popmate.popupstore.domain.PopupStore;
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
public class Reservation extends BaseTime {

    private Long reservationId;
    private PopupStore popupStore;
    private int guestLimit;
    private int currentGuestCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
