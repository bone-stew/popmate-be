package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.application.ReservationInformationService;
import com.bonestew.popmate.reservation.application.dto.ActiveReservationResponse;
import com.bonestew.popmate.reservation.application.dto.DailyReservationResponse;
import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")
public class ReservationInformationController {

    private final ReservationInformationService reservationInformationService;

    /**
     * 진행 중인 예약 정보 조회
     *
     * @param popupStoreId 팝업스토어 식별자
     * @return 예약 여부, 팝업스토어 정보
     */
    @GetMapping("/{popupStoreId}/current-reservations")
    public ApiResponse<ActiveReservationResponse> getActiveReservation(@PathVariable("popupStoreId") Long popupStoreId) {
        return ApiResponse.success(ActiveReservationResponse.from(
            reservationInformationService.getActiveReservation(popupStoreId)
        ));
    }

    /**
     * 일일 예약 목록 조회
     *
     * @param popupStoreId 팝업스토어 식별자
     * @param date         조회 일자
     * @return 예약 목록
     */
    @GetMapping("/{popupStoreId}/reservations")
    public ApiResponse<List<DailyReservationResponse>> getDailyReservations(@PathVariable("popupStoreId") Long popupStoreId,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Reservation> reservations = reservationInformationService.getDailyReservations(popupStoreId, date);
        List<DailyReservationResponse> dailyReservationResponses = reservations.stream()
            .map(DailyReservationResponse::from)
            .toList();
        return ApiResponse.success(dailyReservationResponses);
    }
}
