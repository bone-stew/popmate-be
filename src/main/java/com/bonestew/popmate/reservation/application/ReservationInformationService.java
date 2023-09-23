package com.bonestew.popmate.reservation.application;

import com.bonestew.popmate.reservation.application.dto.GuestLimitUpdateRequest;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.ReservationStatus;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.exception.ReservationNotFoundException;
import com.bonestew.popmate.reservation.exception.UserReservationNotFoundException;
import com.bonestew.popmate.reservation.persistence.ReservationDao;
import com.bonestew.popmate.reservation.persistence.UserReservationDao;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationInformationService {

    private final ReservationDao reservationDao;
    private final UserReservationDao userReservationDao;

    public Reservation getActiveReservation(Long popupStoreId) {
        return reservationDao.findActiveByPopupStoreId(popupStoreId)
            .orElseThrow(() -> new ReservationNotFoundException(popupStoreId));
    }

    public List<Reservation> getDailyReservations(Long popupStoreId, LocalDate date) {
        return reservationDao.findByPopupStoreIdAndStartDate(popupStoreId, date);
    }

    public List<UserReservation> getMyReservations(Long userId) {
        return userReservationDao.getByUserId(userId);
    }

    public UserReservation getMyReservation(Long reservationId, Long userId) {
        return userReservationDao.findByReservationIdAndUserId(reservationId, userId)
            .orElseThrow(() -> new UserReservationNotFoundException(reservationId));
    }

    public Reservation getCurrentlyEnteredReservation(Long popupStoreId) {
        // 현재 입장중인 예약 정보가 없으면 가장 마지막으로 종료된 예약 정보를 반환한다.
        return reservationDao.findByVisitStartTimeLessThanEqualAndVisitEndTimeGreaterThanEqualAndPopupStoreId(popupStoreId)
            .orElseGet(() ->
                reservationDao.findTopByStatusAndPopupStoreIdOrderByEndTimeDesc(popupStoreId)
                    .orElseThrow(() -> new ReservationNotFoundException(popupStoreId))
            );
    }

    public List<Reservation> getTodayReservations(Long popupStoreId) {
        return reservationDao.findByVisitEndTimeGreaterThanEqualAndPopupStoreId(popupStoreId);
    }

    public void updateGuestLimit(Long reservationId, GuestLimitUpdateRequest guestLimitUpdateRequest) {
        if (guestLimitUpdateRequest.guestLimit() <= 0) {
            throw new IllegalArgumentException("guestLimit must be greater than 0");
        }
        reservationDao.updateGuestLimit(reservationId, guestLimitUpdateRequest.guestLimit());
    }

    public void cancelReservation(Long reservationId) {
        reservationDao.updateStatus(reservationId, ReservationStatus.CANCELED);
    }
}
