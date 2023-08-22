package com.bonestew.popmate.order.domain;

import com.bonestew.popmate.date.BaseEntity;
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
public class OrderItem extends BaseEntity {

    private Long orderId;
    private Long tbItemId;
    private int totalQuantity;
    private int totalAmount;
}
