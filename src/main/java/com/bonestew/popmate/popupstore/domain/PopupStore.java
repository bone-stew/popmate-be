package com.bonestew.popmate.popupstore.domain;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.user.domain.User;
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
    private User user;
    private Department department;
    private ChatRoom chatRoomId;
    private Category category;
    private String title;
    private String organizer;
    private String placeDetail;
    private String description;
    private String eventDescription;
    private String bannerImgUrl;
    private int entryFee;
    private int maxCapacity;
    private Boolean reservationEnabled;
    private Boolean salesEnabled;
    private int reservationInterval;
    private int intervalCapacity;
    private int teamSizeLimit;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Long views;
    private LocalDateTime createdAt;
    private int total;
}
