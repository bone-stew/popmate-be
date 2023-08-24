package com.bonestew.popmate.reservation.application;

import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.exception.ReservationNotFoundException;
import com.bonestew.popmate.reservation.persistence.ReservationDao;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationInformationService {

    private final ReservationDao reservationDao;

    public Reservation getActiveReservation(Long popupStoreId) {
        return reservationDao.findActiveByPopupStoreId(popupStoreId)
            .orElseThrow(() -> new ReservationNotFoundException(popupStoreId));
    }

    public List<Reservation> getDailyReservations(Long popupStoreId, LocalDate date) {
        return reservationDao.findByPopupStoreIdAndStartDate(popupStoreId, date);
    }
}
