package com.bonestew.popmate.admin.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MainBanner {
    Long bannerId;
    String imgUrl;
    String popupStoreId;
    LocalDateTime createdAt;
}
