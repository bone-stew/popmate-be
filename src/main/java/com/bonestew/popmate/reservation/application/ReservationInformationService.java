package com.bonestew.popmate.reservation.application;

import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.exception.ReservationNotFoundException;
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
}
