package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationDao {

    Optional<Reservation> findActiveByPopupStoreId(@Param("popupStoreId") Long popupStoreId);

    List<Reservation> findByPopupStoreIdAndStartDate(Long popupStoreId, LocalDate date);
}
