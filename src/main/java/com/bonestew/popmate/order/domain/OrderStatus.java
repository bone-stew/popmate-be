package com.bonestew.popmate.order.domain;

public enum OrderStatus {
    ORDERED(0, "수령대기"),
    PICKED_UP(1, "수령완료"),
    EXPIRED(2, "기간만료"),
    CANCELED(-1, "주문취소");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderStatus from(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown OrderStatus code : " + code);
    }

    public String getDescription() {
        return description;
    }
}
