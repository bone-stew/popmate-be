package com.bonestew.popmate.popupstore.domain;

import com.bonestew.popmate.date.BaseTime;
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
public class PopupStoreSns extends BaseTime {

    private Long snsId;
    private String platform;
    private String url;
}
