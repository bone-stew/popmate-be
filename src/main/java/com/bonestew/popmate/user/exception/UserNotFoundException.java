package com.bonestew.popmate.user.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(Long userId) {
        super("User not found: " + userId);
    }
}
