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
public class Banner {
    
    private Long bannerId;
    private String imgUrl;
    private PopupStore popupStore;
    private LocalDateTime createdAt;
}
