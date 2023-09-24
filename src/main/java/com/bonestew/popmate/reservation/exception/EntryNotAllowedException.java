package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.AccessDeniedException;
import java.time.LocalDateTime;

public class EntryNotAllowedException extends AccessDeniedException {

    public EntryNotAllowedException(LocalDateTime visitStartTime, LocalDateTime visitEndTime) {
        super(String.format("입장 가능 시간이 아닙니다. 입장 가능 시간: %s ~ %s", visitStartTime, visitEndTime));
    }
}
