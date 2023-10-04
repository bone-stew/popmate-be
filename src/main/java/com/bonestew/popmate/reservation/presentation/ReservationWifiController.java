package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.presentation.dto.CheckWifiResponse;
import com.bonestew.popmate.reservation.application.ReservationWifiService;
import com.bonestew.popmate.reservation.application.dto.WifiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationWifiController {

    private final ReservationWifiService reservationWifiService;

    /**
     * 사용자의 wifi 목록을 통해 예약 가능한 위치인지 확인
     *
     * @param reservationId 예약 식별자
     * @param wifiRequest   사용자의 wifi 정보
     * @return 확인 결과
     */
    @PostMapping("/{reservationId}/wifi-check")
    public ApiResponse<CheckWifiResponse> checkWifi(@PathVariable final Long reservationId,
                                                    @RequestBody final WifiRequest wifiRequest) {
        boolean isCheck = reservationWifiService.check(reservationId, wifiRequest);
        return ApiResponse.success(
                new CheckWifiResponse(isCheck)
        );
    }
}
