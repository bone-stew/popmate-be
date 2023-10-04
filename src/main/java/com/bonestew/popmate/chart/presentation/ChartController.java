package com.bonestew.popmate.chart.presentation;


import com.bonestew.popmate.chart.application.ChartService;
import com.bonestew.popmate.chart.presentation.dto.ChartResponse;
import com.bonestew.popmate.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chart")
public class ChartController {

    private final ChartService chartService;

    @GetMapping
//    @Secured({"ROLE_MANAGER"})
    public ApiResponse<ChartResponse> chart() {
        return ApiResponse.success(chartService.getStatistics());
    }

}
