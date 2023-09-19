package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.application.dto.CreateReservationDto;
import com.bonestew.popmate.reservation.domain.Reservation;
import com.bonestew.popmate.reservation.domain.Wifi;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationDao {

    Optional<Reservation> findById(Long reservationId);

    Optional<Reservation> findActiveByPopupStoreId(Long popupStoreId);

    List<Reservation> findByPopupStoreIdAndStartDate(Long popupStoreId, LocalDate date);

    List<Wifi> findWifiById(Long reservationId);

    void updateCurrentGuestCount(Reservation reservation);

    List<Reservation> findAllToInProgress();

    List<Reservation> findAllToClosed();

    void updateReservationStatusToInProgress();

    void updateReservationStatusToClosed();

    void saveAll(CreateReservationDto createReservationDto);

    List<Reservation> findByVisitEndTimeGreaterThanEqualAndPopupStoreId(Long popupStoreId);

    Optional<Reservation> findByVisitStartTimeLessThanEqualAndVisitEndTimeGreaterThanEqualAndPopupStoreId(Long popupStoreId);
}
