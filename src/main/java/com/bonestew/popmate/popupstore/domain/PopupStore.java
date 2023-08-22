package com.bonestew.popmate.popupstore.domain;

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
public class PopupStore {

    private Long popupStoreId;
    private String name;
    // 여기서 부터 시작
    // 위에는 예시 코드를 위에 남겨놓음
    private Long storeId;
    private Long userId;
    private Long departmentId;
    private Long chatRoomId;
    private String title;
    private String placeDetail;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private String description;
    private String eventDescription;
    private int entryFee;
    private String bannerImg;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private int maxCapacity;
    private int reservationSize;
    private int reservationEnabled;
}
