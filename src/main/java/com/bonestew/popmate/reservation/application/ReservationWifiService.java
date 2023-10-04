package com.bonestew.popmate.reservation.application;

import com.bonestew.popmate.reservation.application.dto.WifiRequest;
import com.bonestew.popmate.reservation.domain.Wifi;
import com.bonestew.popmate.reservation.persistence.ReservationDao;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationWifiService {

    private final ReservationDao reservationDao;

    public boolean check(Long reservationId, WifiRequest wifiRequest) {
        List<Wifi> userWifiList = wifiRequest.mapToWifiList();
        List<Wifi> departmentWifiList = reservationDao.findWifiById(reservationId);

        return isUserInLocation(userWifiList, departmentWifiList);
    }

    private boolean isUserInLocation(List<Wifi> userWifiList, List<Wifi> departmentWifiList) {
        return userWifiList.stream()
                .anyMatch(departmentWifiList::contains);
    }
}
