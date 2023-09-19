package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.Wifi;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationDao {

    void saveAll(CreateReservationDto createReservationDto);

    Optional<Reservation> findById(Long reservationId);

    Optional<Reservation> findActiveByPopupStoreId(Long popupStoreId);

    Optional<Reservation> findByVisitStartTimeLessThanEqualAndVisitEndTimeGreaterThanEqualAndPopupStoreId(Long popupStoreId);

    Optional<Reservation> findTopByStatusAndPopupStoreIdOrderByEndTimeDesc(Long popupStoreId);

    List<Reservation> findByPopupStoreIdAndStartDate(Long popupStoreId, LocalDate date);

    List<Reservation> findAllToInProgress();

    List<Reservation> findAllToClosed();

    List<Reservation> findByVisitEndTimeGreaterThanEqualAndPopupStoreId(Long popupStoreId);

    List<Wifi> findWifiById(Long reservationId);

    void updateCurrentGuestCount(Reservation reservation);

    void updateReservationStatusToInProgress();

    void updateReservationStatusToClosed();
}
