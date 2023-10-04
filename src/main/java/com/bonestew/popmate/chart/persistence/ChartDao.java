package com.bonestew.popmate.chart.persistence;

import com.bonestew.popmate.chart.domain.ChartData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChartDao {
    void loadChartData(ChartData data);
}
