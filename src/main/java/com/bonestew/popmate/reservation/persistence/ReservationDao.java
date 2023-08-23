package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.domain.Reservation;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ReservationDao {

    Optional<Reservation> findActiveByPopupStoreId(@Param("popupStoreId") Long popupStoreId);
}
