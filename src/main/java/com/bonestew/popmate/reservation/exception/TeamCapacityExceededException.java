package com.bonestew.popmate.reservation.exception;

import com.bonestew.popmate.exception.BadRequestException;

public class TeamCapacityExceededException extends BadRequestException {

    public TeamCapacityExceededException(int guestCount) {
        super("Team capacity exceeded. guestCount: " + guestCount);
    }
}
