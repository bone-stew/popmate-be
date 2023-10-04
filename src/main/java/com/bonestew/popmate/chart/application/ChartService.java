package com.bonestew.popmate.chart.application;

import com.bonestew.popmate.chart.domain.ChartData;
import com.bonestew.popmate.chart.domain.PopupStoreRank;
import com.bonestew.popmate.chart.domain.ReservationCount;
import com.bonestew.popmate.chart.domain.StoreRevenue;
import com.bonestew.popmate.chart.persistence.ChartDao;
import com.bonestew.popmate.chart.presentation.dto.ChartResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartService {

    private final ChartDao chartDao;

    public ChartResponse getStatistics() {
        ChartData data = new ChartData();
        chartDao.loadChartData(data);
        return new ChartResponse(data.getPopupStoreRanks(), data.getReservationCounts(), data.getStoreRevenues());
    }

}
