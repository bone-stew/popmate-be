package com.bonestew.popmate.popupstore.persistence.dto;

import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.reservation.domain.UserReservation;
import com.bonestew.popmate.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PopupStoreDetailDto {
    PopupStore popupStore;
    Department department;
    UserReservation userReservation;
}
