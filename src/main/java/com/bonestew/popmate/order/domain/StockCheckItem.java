package com.bonestew.popmate.order.domain;

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
public class StockCheckItem {
    Long itemId;
    boolean check;

    public boolean getCheck() {
        return check;
    }
}
