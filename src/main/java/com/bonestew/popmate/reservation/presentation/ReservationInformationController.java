package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.application.ReservationInformationService;
import com.bonestew.popmate.reservation.application.dto.GuestLimitUpdateRequest;
import com.bonestew.popmate.reservation.application.dto.ReservationEntranceResponse;
import com.bonestew.popmate.reservation.presentation.dto.MyReservationResponse;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.presentation.dto.ActiveReservationResponse;
import com.bonestew.popmate.reservation.presentation.dto.DailyReservationResponse;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.presentation.dto.MyReservationsResponse;
import com.bonestew.popmate.reservation.presentation.dto.TodayReservationResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReservationInformationController {

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
     * 나의 예약 목록 조회
     *
     * @return 예약 목록
     */
    @GetMapping("/members/me/reservations")
    public ApiResponse<MyReservationsResponse> getMyReservations(@AuthenticationPrincipal PopmateUser popmateUser) {
        List<UserReservation> reservations = reservationInformationService.getMyReservations(popmateUser.getUserId());
        return ApiResponse.success(
            MyReservationsResponse.from(reservations)
        );
    }

    /**
     * 나의 예약 상세 조회 (추후 reservations -> userReservation 으로 변경 필요)
     *
     * @param reservationId 예약 식별자
     * @return 예약 정보
     */
    @GetMapping("/members/me/reservations/{reservationId}")
    public ApiResponse<MyReservationResponse> getReservation(@PathVariable("reservationId") Long reservationId,
                                                             @AuthenticationPrincipal PopmateUser popmateUser) {
        return ApiResponse.success(
            MyReservationResponse.from(
                reservationInformationService.getMyReservation(reservationId, popmateUser.getUserId())
            ));
    }

    /**
     * 일일 예약 목록 조회 (관리자)
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
     * 오늘의 예약 목록 조회 (관리자)
     *
     * @param popupStoreId 팝업스토어 식별자
     * @return 오늘의 예약 목록
     */
    @GetMapping("/popup-stores/{popupStoreId}/reservations/today")
    public ApiResponse<TodayReservationResponse> getTodayReservations(@PathVariable("popupStoreId") Long popupStoreId) {
        Reservation activeReservation = reservationInformationService.getCurrentlyEnteredReservation(popupStoreId);
        List<Reservation> reservations = reservationInformationService.getTodayReservations(popupStoreId);
        return ApiResponse.success(
            TodayReservationResponse.of(activeReservation, reservations)
        );
    }

    /**
     * 예약 인원 수 수정 (관리자)
     *
     * @param reservationId           예약 식별자
     * @param guestLimitUpdateRequest 예약 요청 정보
     */
    @PatchMapping("/reservations/{reservationId}/guest-limit")
    public ApiResponse<Void> updateReservationGuestCount(@PathVariable("reservationId") Long reservationId,
                                                         @RequestBody GuestLimitUpdateRequest guestLimitUpdateRequest) {
        reservationInformationService.updateGuestLimit(reservationId, guestLimitUpdateRequest);
        return ApiResponse.success();
    }

    /**
     * 예약 중단 (관리자)
     *
     * @param reservationId 예약 식별자
     */
    @PatchMapping("/reservations/{reservationId}/cancellation")
    public ApiResponse<Void> cancelReservation(@PathVariable("reservationId") Long reservationId) {
        reservationInformationService.cancelReservation(reservationId);
        return ApiResponse.success();
    }

    /**
     * 예약 재개 (관리자)
     *
     * @param reservationId 예약 식별자
     */
    @PatchMapping("/reservations/{reservationId}/resume")
    public ApiResponse<Void> resumeReservation(@PathVariable("reservationId") Long reservationId) {
        reservationInformationService.resumeReservation(reservationId);
        return ApiResponse.success();
    }

    /**
     * 예약자 입장 정보 조회 (관리자)
     *
     * @param reservationId 예약 식별자
     * @return 예약 정보
     */
    @GetMapping("/reservations/{reservationId}/entrance-info")
    public ApiResponse<List<ReservationEntranceResponse>> getEntranceInfo(@PathVariable("reservationId") Long reservationId) {
        List<UserReservation> reservations = reservationInformationService.getEntranceInfo(reservationId);
        return ApiResponse.success(
            reservations.stream()
                .map(ReservationEntranceResponse::from)
                .toList()
        );
    }
}
