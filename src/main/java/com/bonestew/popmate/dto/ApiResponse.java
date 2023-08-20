package com.bonestew.popmate.dto;

import com.bonestew.popmate.exception.enums.ResultCode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class ApiResponse<T> {

    private final String code;
    private final String message;
    private final T data;

    public ApiResponse(ResultCode code, String message, T data) {
        this.code = code.name();
        this.message = message;
        this.data = data;
    }

    public ApiResponse(ResultCode code, String message) {
        this(code, message, null);
    }

    public ApiResponse(ResultCode code) {
        this(code, null, null);
    }

    public ApiResponse(ResultCode code, T data) {
        this(code, code.getMessage(), data);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResultCode.SUCCESS);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResultCode.SUCCESS, data);
    }

    public static <T> ApiResponse<T> failure() {
        return new ApiResponse<>(ResultCode.FAILURE);
    }

    public static <T> ApiResponse<T> failure(ResultCode resultCode) {
        return new ApiResponse<>(resultCode);
    }

    public static <T> ApiResponse<T> failure(ResultCode resultCode, String message) {
        return new ApiResponse<>(resultCode, message);
    }
}
