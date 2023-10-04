package com.bonestew.popmate.chart.domain;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
public class ReservationCount {
    private LocalDateTime time;
    private Integer count;
}
