package com.bonestew.popmate.reservation.application;

import static com.bonestew.popmate.popupstore.config.FolderType.RESERVATIONS;

import com.bonestew.popmate.popupstore.config.service.FileService;
import com.bonestew.popmate.user.application.UserService;
import com.bonestew.popmate.user.domain.User;
import com.bonestew.popmate.reservation.application.dto.ReservationRequest;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.exception.AlreadyReservedException;
import com.bonestew.popmate.reservation.exception.ReservationNotFoundException;
import com.bonestew.popmate.reservation.persistence.ReservationDao;
import com.bonestew.popmate.reservation.persistence.UserReservationDao;
import java.io.InputStream;
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
    private final UserService userService;
    private final QrService qrService;
    private final FileService fileService;

    @Transactional
    public void reserve(final Long reservationId, final Long userId, final ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        User user = userService.getUserById(userId);

        // TODO: 사용자가 예약 가능한 위치인지 확인

        reservation.validate(reservationRequest.guestCount());
        if (userReservationDao.existsByUserIdAndReservationId(userId, reservationId)) {
            throw new AlreadyReservedException(userId, reservationId);
        }

        reservation.increaseCurrentGuestCount(reservationRequest.guestCount());
        reservationDao.updateCurrentGuestCount(reservation);

        String reservationQrCode = generateReservationQrCode(user, reservation);

        userReservationDao.save(UserReservation.of(
            user, reservation, reservationQrCode, reservationRequest.guestCount()
        ));

        log.info("Reservation successful for user ID: {}, reservation ID: {}", user.getUserId(), reservationId);
    }

    private String generateReservationQrCode(User user, Reservation reservation) {
        InputStream inputStream = qrService.generateQRCode(user.getUserId(), reservation.getReservationId());
        String directory = String.format(RESERVATIONS.getFolderName(),
            reservation.getPopupStore().getPopupStoreId(), reservation.getReservationId());

        return fileService.uploadInputStream(inputStream, directory);
    }
}
