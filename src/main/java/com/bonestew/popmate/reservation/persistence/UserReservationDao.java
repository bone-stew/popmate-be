package com.bonestew.popmate.reservation.persistence;

import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserReservationDao {

    void save(UserReservation userReservation);

    boolean existsByUserIdAndReservationId(Long userId, Long reservationId, UserReservationStatus status);

    Optional<UserReservation> findByReservationIdAndUserId(Long reservationId, Long userId);

    Optional<UserReservation> findByReservationIdAndUserIdAndStatus(Long reservationId,
                                                                    Long userId,
                                                                    UserReservationStatus status);

    List<UserReservation> getByUserId(Long userId);

    List<UserReservation> findByReservationId(Long reservationId);

    void updateStatus(Long userReservationId, UserReservationStatus status);
}
