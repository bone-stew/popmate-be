package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.application.ReservationInformationService;
import com.bonestew.popmate.reservation.presentation.dto.MyReservationResponse;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.presentation.dto.ActiveReservationResponse;
import com.bonestew.popmate.reservation.presentation.dto.DailyReservationResponse;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.presentation.dto.MyReservationsResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReservationInformationController {

    /**
     * 임시 유저 식별자
     */
    private static final Long USER_ID = 1L;

    private final ReservationInformationService reservationInformationService;

    /**
     * 진행 중인 예약 정보 조회
     *
     * @param popupStoreId 팝업스토어 식별자
     * @return 예약 여부, 팝업스토어 정보
     */
    @GetMapping("/popup-stores/{popupStoreId}/current-reservations")
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
    @GetMapping("/popup-stores/{popupStoreId}/reservations")
    public ApiResponse<List<DailyReservationResponse>> getDailyReservations(@PathVariable("popupStoreId") Long popupStoreId,
                                                                            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        List<Reservation> reservations = reservationInformationService.getDailyReservations(popupStoreId, date);
        List<DailyReservationResponse> dailyReservationResponses = reservations.stream()
            .map(DailyReservationResponse::from)
            .toList();
        return ApiResponse.success(dailyReservationResponses);
    }

    /**
     * 내가 예약한 목록 조회
     *
     * @return 예약 목록
     */
    @GetMapping("members/me/reservations")
    public ApiResponse<MyReservationsResponse> getMyReservations(@AuthenticationPrincipal PopmateUser popmateUser) {
        System.out.println("popmateUser = " + popmateUser.getUserId());
        List<UserReservation> reservations = reservationInformationService.getMyReservations(USER_ID);
        return ApiResponse.success(
            MyReservationsResponse.from(reservations)
        );
    }

    /**
     * 나의 예약 상세 조회
     *
     * @param reservationId 예약 식별자
     * @return 예약 정보
     */
    @GetMapping("/reservations/{reservationId}")
    public ApiResponse<MyReservationResponse> getReservation(@PathVariable("reservationId") Long reservationId) {
        return ApiResponse.success(MyReservationResponse.from(
            reservationInformationService.getMyReservation(reservationId, USER_ID)
        ));
    }
}
