package com.bonestew.popmate.reservation.application.dto;

import java.util.List;

public record ReservationRequest(
    int guestCount,
    List<WifiInfoRequest> wifiList
) {

}
