package com.bonestew.popmate.reservation.domain;

import java.util.Arrays;

public enum ReservationStatus {

    CANCELED(0, "예약 취소"),

    PENDING(1, "예약 대기"),

    IN_PROGRESS(2, "예약 진행"),

    CONFIRMED(3, "예약 완료");

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
