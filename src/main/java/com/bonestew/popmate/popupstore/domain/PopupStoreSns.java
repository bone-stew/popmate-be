package com.bonestew.popmate.popupstore.domain;

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
public class PopupStoreSns {

    private Long snsId;
    private Long storeId;
    private String platform;
    private String snsUrl;
}
