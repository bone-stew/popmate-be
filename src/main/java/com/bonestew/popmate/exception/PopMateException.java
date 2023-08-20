package com.bonestew.popmate.exception;

public class PopMateException extends RuntimeException {

    public PopMateException(String message) {
        super(message);
    }

    public PopMateException(String message, Throwable cause) {
        super(message, cause);
    }
}
