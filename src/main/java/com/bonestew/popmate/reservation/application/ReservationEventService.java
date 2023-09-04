package com.bonestew.popmate.reservation.application;

import com.bonestew.popmate.auth.domain.User;
import com.bonestew.popmate.reservation.application.dto.ReservationRequest;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.exception.AlreadyReservedException;
import com.bonestew.popmate.reservation.exception.ReservationNotFoundException;
import com.bonestew.popmate.reservation.persistence.ReservationDao;
import com.bonestew.popmate.reservation.persistence.UserReservationDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationEventService {

    private final ReservationDao reservationDao;
    private final UserReservationDao userReservationDao;

    @Transactional
    public void reserve(final Long reservationId, final Long userId, final ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        User user = new User(userId, null, null, null, null, null, null); // 임시 (userDao 필요)

        // TODO: 사용자가 예약 가능한 위치인지 확인

        reservation.validate(reservationRequest.guestCount());
        if (userReservationDao.existsByUserIdAndReservationId(userId, reservationId)) {
            throw new AlreadyReservedException(userId, reservationId);
        }

        reservation.increaseCurrentGuestCount(reservationRequest.guestCount());
        reservationDao.updateCurrentGuestCount(reservation);

        // TODO: QR 코드 생성
        String qrImgUrl = "Sample URL";

        userReservationDao.save(UserReservation.of(
            user, reservation, qrImgUrl, reservationRequest.guestCount()
        ));

        log.info("Reservation successful for user ID: {}, reservation ID: {}", user.getUserId(), reservationId);
    }
}
