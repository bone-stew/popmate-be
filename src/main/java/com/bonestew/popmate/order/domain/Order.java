package com.bonestew.popmate.order.domain;

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
public class Order {

    private Long orderId;
    private Long userId;
    private Long storeId;
    private LocalDateTime orderDate;
    private int status;
    private int totalAmount;
}
