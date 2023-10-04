package com.bonestew.popmate.reservation.application.dto;

import com.bonestew.popmate.reservation.domain.Wifi;

public record WifiInfoRequest(
        String ssid,
        String bssid
) {

    public Wifi mapToWifi() {
        return new Wifi(null, null, ssid, bssid);
    }
}
