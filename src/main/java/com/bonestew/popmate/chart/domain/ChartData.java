package com.bonestew.popmate.chart.domain;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class ChartData {

    List<PopupStoreRank> popupStoreRanks;
    List<ReservationCount> reservationCounts;
    List<StoreRevenue> storeRevenues;
}
