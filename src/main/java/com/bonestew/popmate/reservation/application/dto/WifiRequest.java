package com.bonestew.popmate.reservation.application.dto;

import com.bonestew.popmate.reservation.domain.Wifi;
import java.util.List;

public record WifiRequest(
        List<WifiInfoRequest> wifiList
) {

    public List<Wifi> mapToWifiList() {
        return wifiList.stream()
                .map(WifiInfoRequest::mapToWifi)
                .toList();
    }
}
