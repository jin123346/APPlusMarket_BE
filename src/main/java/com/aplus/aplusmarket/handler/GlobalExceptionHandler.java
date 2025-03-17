package com.aplus.aplusmarket.handler;


import com.aplus.aplusmarket.dto.ResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO<?>> handleCustomException(CustomException ex) {

        // Kafka Consumer 예외일 경우 추가 로그 및 처리
        if (ex.getCustomCode() == ResponseCode.KAFKA_SEARCH_IN_ERROR) {
            log.error("Kafka Consumer 내부 오류 발생!");
        }
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ResponseDTO.error(ex.getCustomCode(),ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<?>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(ResponseCode.INTERNAL_SERVER_ERROR.getHttpCode())
                .body(ResponseDTO.error(ResponseCode.INTERNAL_SERVER_ERROR, "서버 내부 오류"));
    }
}
