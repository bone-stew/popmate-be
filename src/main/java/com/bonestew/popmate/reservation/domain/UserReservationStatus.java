package com.bonestew.popmate.reservation.domain;

import java.util.Arrays;

public enum UserReservationStatus {

    CANCELED(-1, "예약 취소"),

    RESERVED(0, "예약 완료"),

    VISITED(1, "입장 완료");

    private final int code;
    private final String description;

    UserReservationStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static UserReservationStatus fromCode(int code) {
        return Arrays.stream(UserReservationStatus.values())
            .filter(status -> status.code == code)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown ReservationStatus code: " + code));
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isReserved() {
        return this == RESERVED;
    }
}
