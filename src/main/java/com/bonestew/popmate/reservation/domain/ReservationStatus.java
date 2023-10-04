package com.bonestew.popmate.reservation.domain;

import java.util.Arrays;

public enum ReservationStatus {

    SCHEDULED(0, "예약 예정"),

    IN_PROGRESS(1, "예약 중"),

    CLOSED(2, "예약 마감"),

    ENTERING(3, "입장 중"),

    ENTERED(4, "입장 완료"),

    CANCELED(-1, "예약 중단");

    private final int code;
    private final String description;

    ReservationStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ReservationStatus fromCode(int code) {
        return Arrays.stream(ReservationStatus.values())
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
}
