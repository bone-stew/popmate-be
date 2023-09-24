package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.domain.UserReservation;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserReservationDao {

    List<UserReservation> getByUserId(Long userId);

    void save(UserReservation userReservation);

    boolean existsByUserIdAndReservationId(Long userId, Long reservationId);

    Optional<UserReservation> findByReservationIdAndUserId(Long reservationId, Long userId);

    int changeStatus(Long reservationId);
}
