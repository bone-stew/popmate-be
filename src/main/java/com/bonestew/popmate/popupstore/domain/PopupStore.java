package com.bonestew.popmate.popupstore.domain;

import com.bonestew.popmate.chat.domain.ChatRoom;
import com.bonestew.popmate.date.BaseTime;
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
public class PopupStore extends BaseTime {

    private Long popupStoreId;
    private User user;
    private Department departmentId;
    private ChatRoom chatRoomId;
    private String title;
    private String placeDetail;
    private String description;
    private String eventDescription;
    private String bannerImUrl;
    private int entryFee;
    private int maxCapacity;
    private int reservationSize;
    private boolean reservationEnabled;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
    private LocalDateTime openTime;
    private LocalDateTime closeTime;
}
