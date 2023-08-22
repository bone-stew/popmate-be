package com.bonestew.popmate.popupstore.domain;

import com.bonestew.popmate.date.BaseEntity;
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
public class MainBannerImg extends BaseEntity {
    
    private Long bannerId;
    private String imgUrl;
    private Long storeId;
}
