package com.bonestew.popmate.chart.domain;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StoreRevenue {

    private Long popupStoreId;
    private String title;
    private Integer revenue;
}
