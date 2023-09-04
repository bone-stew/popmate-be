package com.bonestew.popmate.popupstore.persistence.dto;

import com.bonestew.popmate.popupstore.domain.Department;
import com.bonestew.popmate.popupstore.domain.PopupStore;
import com.bonestew.popmate.reservation.domain.UserReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PopupStoreDetailDto {
    PopupStore popupStore;
    Department department;
    UserReservationStatus userReservationStatus;
}
