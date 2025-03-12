package com.aplus.aplusmarket.handler;


import com.aplus.aplusmarket.dto.ResponseDTO;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<?>> handleCustomException(CustomException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ResponseDTO.error(ex.getCustomCode(),ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<?>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(ResponseCode.PAYMENT_SYSTEM_ERROR.getHttpCode())
                .body(ResponseDTO.error(ResponseCode.PAYMENT_SYSTEM_ERROR, "서버 내부 오류"));
    }
}
