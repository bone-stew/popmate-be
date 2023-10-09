package com.bonestew.popmate.reservation.application;

import static com.bonestew.popmate.popupstore.config.FolderType.RESERVATIONS;

import com.bonestew.popmate.annotation.DistributedLock;
import com.bonestew.popmate.popupstore.config.service.FileService;
import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.application.dto.ProcessEntranceRequest;
import com.bonestew.popmate.reservation.application.dto.WifiRequest;
import com.bonestew.popmate.reservation.domain.ReservationStatus;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import com.bonestew.popmate.reservation.exception.InvalidReservationCancellationException;
import com.bonestew.popmate.reservation.exception.UserReservationNotFoundException;
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
    @DistributedLock(key = "#reservationId")
    public Long reserve(final Long reservationId, final Long userId, final ReservationRequest reservationRequest) {
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

        if (userReservationDao.existsByUserIdAndReservationId(userId, reservationId, UserReservationStatus.RESERVED)) {
            throw new AlreadyReservedException(userId, reservationId);
        }

        reservation.increaseCurrentGuestCount(reservationRequest.guestCount());
        reservationDao.updateCurrentGuestCount(reservation.getReservationId(), reservation.getCurrentGuestCount());

        String reservationQrCode = generateReservationQrCode(user, reservation);

        UserReservation userReservation = UserReservation.of(
            user, reservation, reservationQrCode, reservationRequest.guestCount()
        );
        userReservationDao.save(userReservation);

        log.info("Reservation successful for user ID: {}, reservation ID: {}", user.getUserId(), reservationId);

        return userReservation.getUserReservationId();
    }

    private String generateReservationQrCode(final User user, final Reservation reservation) {
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
        List<Reservation> scheduledReservations = reservationDao.findAllToInProgress();
        List<Reservation> inProgressReservations = reservationDao.findAllToClosed();
        List<Reservation> closedReservations = reservationDao.findAllToEntering();
        List<Reservation> enteringReservations = reservationDao.findAllToEntered();

        reservationDao.updateReservationStatusToInProgress();
        reservationDao.updateReservationStatusToClosed();
        reservationDao.updateReservationStatusToEntering();
        reservationDao.updateReservationStatusToEntered();

        scheduledReservations.forEach(
            reservation -> logChangedReservations(reservation, ReservationStatus.IN_PROGRESS));
        inProgressReservations.forEach(reservation -> logChangedReservations(reservation, ReservationStatus.CLOSED));
        closedReservations.forEach(reservation -> logChangedReservations(reservation, ReservationStatus.ENTERED));
        enteringReservations.forEach(reservation -> logChangedReservations(reservation, ReservationStatus.ENTERING));
    }

    private void logChangedReservations(Reservation reservation, ReservationStatus reservationStatus) {
        String format = "Changing reservation status to {}. [Reservation ID: {}]";
        log.info(format, reservationStatus.name(), reservation.getReservationId());
    }

    /**
     * 예약 취소
     *
     * @param userReservationId
     */
    @Transactional
    public void cancel(final Long userReservationId) {
        UserReservation userReservation = userReservationDao.findById(userReservationId)
            .orElseThrow(() -> new UserReservationNotFoundException(userReservationId));

        if (!userReservation.getStatus().isReserved()) {
            throw new InvalidReservationCancellationException(userReservation.getUserReservationId());
        }
        userReservationDao.updateStatus(userReservation.getUserReservationId(), UserReservationStatus.CANCELED);

        Reservation reservation = userReservation.getReservation();
        reservation.decreaseCurrentGuestCount(userReservation.getGuestCount());
        reservationDao.updateCurrentGuestCount(
            reservation.getReservationId(),
            reservation.getCurrentGuestCount()
        );

        log.info("Cancel successful for user ID: {}, reservationId ID: {}, userReservation ID: {}",
            userReservation.getUser().getUserId(),
            reservation.getReservationId(),
            userReservationId);
    }

    /**
     * 예약자 입장 처리
     *
     * @param reservationId
     * @param processEntranceRequest
     */
    @Transactional
    public void processEntrance(Long reservationId, ProcessEntranceRequest processEntranceRequest) {
        Long userId = processEntranceRequest.reservationUserId();
        UserReservation userReservation = userReservationDao.findByReservationIdAndUserIdAndStatus(reservationId,
                userId, UserReservationStatus.RESERVED)
            .orElseThrow(() -> new UserReservationNotFoundException(reservationId));

        userReservation.validateEntry();
        userReservationDao.updateStatus(userReservation.getUserReservationId(), UserReservationStatus.VISITED);

        log.info("Entrance successful for user ID: {}, reservation ID: {}", userId, reservationId);
    }

    public void deleteReservationFromTomorrow(Long popupStoreId) {
        reservationDao.deleteReservationFromTomorrow(popupStoreId);
    }
}
