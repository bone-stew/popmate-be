package com.bonestew.popmate.exception.handler;

import com.bonestew.popmate.dto.ApiResponse;
import com.bonestew.popmate.exception.PopMateException;
import com.bonestew.popmate.exception.enums.ResultCode;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.Errors;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("handleMethodArgumentNotValidException: ", e);
        return ApiResponse.failure(
            ResultCode.BAD_REQUEST,
            Optional.of(e.getBindingResult())
                .map(Errors::getFieldErrors)
                .map(errors -> errors.stream()
                    .map(it -> it.getDefaultMessage() + ". " + it.getField() + ": " + it.getRejectedValue())
                    .collect(Collectors.joining(", ")))
                .orElseGet(e::getMessage)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.info("HttpMessageNotReadableException: ", e);
        return ApiResponse.failure(ResultCode.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("handleHttpRequestMethodNotSupportedException: ", e);
        return ApiResponse.failure(ResultCode.METHOD_NOT_ALLOWED, e.getBody().getTitle());
    }

    @ExceptionHandler({
        PopMateException.class,
        Exception.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleException(Exception e) {
        log.error("handleException: ", e);
        return ApiResponse.failure(ResultCode.INTERNAL_SERVER_ERROR);
    }
}
