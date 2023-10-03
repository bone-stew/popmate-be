package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.application.ReservationEventService;
import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.application.dto.ProcessEntranceRequest;
import com.bonestew.popmate.reservation.application.dto.ReservationRequest;
import com.bonestew.popmate.reservation.presentation.dto.CreateUserReservationResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
public class ReservationEventController {

    private final ReservationEventService reservationEventService;

    /**
     * 선착순 예약 신청
     *
     * @param reservationId      예약 식별자
     * @param reservationRequest 예약 요청 정보
     * @return 예약 신청 결과 (예약 성공했을 경우 예약자 식별자)
     */
    @PostMapping("/{reservationId}")
    public ApiResponse<CreateUserReservationResponse> reserve(@PathVariable("reservationId") final Long reservationId,
                                                              @RequestBody final ReservationRequest reservationRequest,
                                                              @AuthenticationPrincipal PopmateUser popmateUser) {
        return ApiResponse.success(
            new CreateUserReservationResponse(
                reservationEventService.reserve(reservationId, popmateUser.getUserId(), reservationRequest))
        );
    }

    /**
     * 예약 취소
     *
     * @param reservationId
     */
    @PatchMapping("/{reservationId}/cancel")
    public ApiResponse<Void> cancelReservation(@PathVariable("reservationId") Long reservationId,
                                               @AuthenticationPrincipal PopmateUser popmateUser) {
        reservationEventService.cancel(reservationId, popmateUser.getUserId());
        return ApiResponse.success();
    }

    /**
     * 입장 처리 (관리자)
     *
     * @param reservationId
     * @return 예약자 입장 처리 결과
     */
    @PatchMapping("/{reservationId}/entrance")
    public ApiResponse<Void> processEntrance(@PathVariable("reservationId") Long reservationId,
                                             @RequestBody ProcessEntranceRequest processEntranceRequest) {
        reservationEventService.processEntrance(reservationId, processEntranceRequest);
        return ApiResponse.success();
    }

    /**
     * 추후 popupstore-api에서 호출 예정
     */
    @Deprecated
    @GetMapping("/test")
    public void createReservation() {
        CreateReservationDto sampleReservationDto = new CreateReservationDto(
            20,
            LocalDateTime.of(2023, 9, 25, 10, 0),
            LocalDateTime.of(2023, 9, 25, 20, 0),
            8L,
            50,
            6);
        reservationEventService.createReservation(sampleReservationDto);
    }
}
