package com.bonestew.popmate.auth.exception;

import com.bonestew.popmate.exception.NotFoundException;

public class BackOfficeUserNotFoundException extends NotFoundException {

    public BackOfficeUserNotFoundException(String message) {
        super("유효하지 않은 아이디와 비밀번호입니다.");
    }
}
