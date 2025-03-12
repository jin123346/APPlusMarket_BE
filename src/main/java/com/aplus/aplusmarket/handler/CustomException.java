package com.aplus.aplusmarket.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ResponseCode customCode;

    public CustomException(HttpStatus httpStatus, ResponseCode customCode) {
        super(customCode.getMessage());
        this.httpStatus = httpStatus;
        this.customCode = customCode;
    }

    public CustomException(ResponseCode customCode) {
        this.httpStatus = customCode.getHttpStatus();
        this.customCode = customCode;

    }

    public CustomException(HttpStatus httpStatus, ResponseCode customCode, String customMessage) {
        super(customMessage);
        this.httpStatus = httpStatus;
        this.customCode = customCode;
    }
}

