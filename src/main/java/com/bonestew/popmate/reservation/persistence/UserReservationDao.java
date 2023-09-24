package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.domain.UserReservation;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserReservationDao {

    void save(UserReservation userReservation);

    boolean existsByUserIdAndReservationId(Long userId, Long reservationId);

    Optional<UserReservation> findByReservationIdAndUserId(Long reservationId, Long userId);

    List<UserReservation> getByUserId(Long userId);

    List<UserReservation> findByReservationId(Long reservationId);

    int changeStatus(Long reservationId);
}
