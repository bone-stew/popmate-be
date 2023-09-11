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
public class PopupStoreItem {

    private Long popupStoreItemId;
    private PopupStore popupStore;
    private String name;
    private String imgUrl;
    private int amount;
    private int stock;
    private int orderLimit;
    private LocalDateTime createdAt;
}
