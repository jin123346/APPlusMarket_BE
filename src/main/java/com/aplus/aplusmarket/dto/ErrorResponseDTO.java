package com.aplus.aplusmarket.dto;

import lombok.*;
/*
    2024.1.26 하진희 :  responseDTO (에러시)
 */
@Getter
@NoArgsConstructor
@ToString
public class ErrorResponseDTO extends  ResponseDTO {

        public ErrorResponseDTO(Integer code, String message) {
            super("failed",code, message);
        }

        public static ErrorResponseDTO of(Integer code, String message) {
            return new ErrorResponseDTO(code, message);
        }



}
