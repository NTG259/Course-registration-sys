package com.web.registration.exception;

public record ErrorResponse(String code, int httpStatus, String message) {

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getHttpStatus(), errorCode.getMessage());
    }
}
