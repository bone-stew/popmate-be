package com.bonestew.popmate.popupstore.domain;

import com.bonestew.popmate.date.BaseEntity;
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
public class Department extends BaseEntity {
    
    private Long departmentId;
    private String name;
    private String placeDescription;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private int latitude;
    private int longitude;
}
