package com.bonestew.popmate.reservation.application;

import static com.bonestew.popmate.popupstore.config.FolderType.RESERVATIONS;

import com.bonestew.popmate.popupstore.config.service.FileService;
import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.application.dto.WifiRequest;
import com.bonestew.popmate.reservation.domain.ReservationStatus;
import com.bonestew.popmate.reservation.exception.WifiCheckException;
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
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationEventService {

    private final ReservationDao reservationDao;
    private final UserReservationDao userReservationDao;
    private final ReservationWifiService reservationWifiService;
    private final UserService userService;
    private final QrService qrService;
    private final FileService fileService;

    /**
     * 선착순 예약 신청
     *
     * @param reservationId
     * @param userId
     * @param reservationRequest
     */
    @Transactional
    public void reserve(final Long reservationId, final Long userId, final ReservationRequest reservationRequest) {
        Reservation reservation = reservationDao.findById(reservationId)
            .orElseThrow(() -> new ReservationNotFoundException(reservationId));
        User user = userService.getUserById(userId);
        boolean isCheck = reservationWifiService.check(
            reservationId,
            new WifiRequest(reservationRequest.wifiList())
        );
        if (!isCheck) {
            throw new WifiCheckException();
        }

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

    /**
     * 팝업스토어 생성 시 예약 정보를 생성하는 프로시저를 실행하는 메서드
     */
    @Transactional
    public void createReservation(CreateReservationDto createReservationDto) {
        reservationDao.saveAll(createReservationDto);
    }

    /**
     * 예약 상태를 변경하는 스케줄러 (매일 9시부터 22시까지 매 분 0초에 실행)
     */
    @Transactional
    @Scheduled(cron = "0 * 9-22 * * *")
    public void changeReservationStatus() {
        List<Reservation> activeReservations = reservationDao.findAllToInProgress();
        List<Reservation> closedReservations = reservationDao.findAllToClosed();

        activeReservations.forEach(reservation -> logChangedReservations(reservation, ReservationStatus.IN_PROGRESS));
        closedReservations.forEach(reservation -> logChangedReservations(reservation, ReservationStatus.CLOSED));

        reservationDao.updateReservationStatusToInProgress();
        reservationDao.updateReservationStatusToClosed();
    }

    private void logChangedReservations(Reservation reservation, ReservationStatus status) {
        String format = "Changing reservation status to {}. [Reservation ID: {}]";
        switch (status) {
            case IN_PROGRESS -> log.info(format, ReservationStatus.IN_PROGRESS.name(), reservation.getReservationId());
            case CLOSED -> log.info(format, ReservationStatus.CLOSED.name(), reservation.getReservationId());
            default -> throw new IllegalArgumentException("Unknown ReservationStatus code: " + status);
        }
    }
}
