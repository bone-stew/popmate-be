package com.bonestew.popmate.reservation.presentation;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.reservation.application.ReservationInfoService;
import com.bonestew.popmate.reservation.application.dto.ActiveReservationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/popup-stores")
public class ReservationInformationController {

    private final ReservationInfoService reservationInfoService;

    @GetMapping("/{popupStoreId}/current-reservations")
    public ApiResponse<ActiveReservationResponse> getActiveReservation(@PathVariable("popupStoreId") Long popupStoreId) {
        return ApiResponse.success(ActiveReservationResponse.from(
            reservationInfoService.getActiveReservation(popupStoreId)
        ));
    }
}
