package com.bonestew.popmate.chart.presentation.dto;

import com.bonestew.popmate.chart.domain.PopupStoreRank;
import com.bonestew.popmate.chart.domain.ReservationCount;
import com.bonestew.popmate.chart.domain.StoreRevenue;

import java.util.List;

public record ChartResponse(
        List<PopupStoreRank> popupStoreRanks,
        List<ReservationCount> reservationCounts,
        List<StoreRevenue> storeRevenues
) {
}
