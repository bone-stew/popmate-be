package com.bonestew.popmate.popupstore.domain;

import com.bonestew.popmate.date.BaseTime;
import java.time.LocalDateTime;
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
public class Department extends BaseTime {
    
    private Long departmentId;
    private String name;
    private String placeDescription;
    private double latitude;
    private double longitude;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
}
