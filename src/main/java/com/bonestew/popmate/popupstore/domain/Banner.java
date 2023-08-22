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
public class Banner extends BaseTime {
    
    private Long bannerId;
    private String imgUrl;
    private PopupStore popupStore;
}
