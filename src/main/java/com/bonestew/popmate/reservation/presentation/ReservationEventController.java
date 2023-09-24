package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.auth.domain.PopmateUser;
import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.application.ReservationEventService;
import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.application.dto.ReservationRequest;
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
     */
    @PostMapping("/{reservationId}")
    public ApiResponse<Void> reserve(@PathVariable("reservationId") final Long reservationId,
                                     @RequestBody final ReservationRequest reservationRequest,
                                     @AuthenticationPrincipal PopmateUser popmateUser) {
        reservationEventService.reserve(reservationId, popmateUser.getUserId(), reservationRequest);
        return ApiResponse.success();
    }

    /**
     * 추후 popupstore-api에서 호출 예정
     */
    @Deprecated
    @GetMapping("/test")
    public void createReservation() {
        CreateReservationDto sampleReservationDto = new CreateReservationDto(
            15,
            LocalDateTime.of(2023, 9, 11, 10, 0),
            LocalDateTime.of(2023, 9, 22, 20, 0),
            21L,
            50,
            6);
        reservationEventService.createReservation(sampleReservationDto);
    }

    @PatchMapping("/{reservationId}/entry")
    public ApiResponse<String> changeStatus(@PathVariable("reservationId") Long reservationId){
        String message = reservationEventService.changeStatus(reservationId);
        return ApiResponse.success(
            message
        );
    }
}
