package com.bonestew.popmate.popupstore.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PopupStoreSearchRequest {

    Boolean isOpeningSoon;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime endDate;
    String keyword;
    int searchLimit; // 무한 스크롤
}