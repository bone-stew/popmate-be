package com.bonestew.popmate.reservation.domain;

import com.bonestew.popmate.popupstore.domain.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Wifi {

    private Long wifiId;
    private Department department;
    private String ssid;
    private String bssid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wifi wifi)) {
            return false;
        }
        return  getBssid().equals(wifi.getBssid());
    }
}
