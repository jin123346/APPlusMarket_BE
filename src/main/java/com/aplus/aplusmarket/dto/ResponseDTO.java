package com.aplus.aplusmarket.dto;

import lombok.*;
/*
    2024.1.26 하진희 :  responseDTO (기본시 )
 */
@Builder
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private  String status;
    private  Integer code;
    private  String message;

    /*
    예시
    코드   / http status /
    1000	    200	        로그인 성공
    1001	    401	        비밀번호 오류
    1002	    403	        계정 잠김
    1003	    404	        존재하지 않는 계정
    1100	    201	        회원가입 성공
    1101	    400	        이메일 중복
    1102	    400	        약관 미동의
    1103	    201	        회원가입 실패
    2000	    201	        상품 등록 성공
    2001	    400	        입력 데이터 유효성 오류
    3000	    200     	거래 성공
    3001	    400     	잔액 부족
    3002	    400     	거래 제한
    4000	    200	        메시지 전송 성공
    4001	    403	        상대방이 차단한 사용자
    4002	    400	        메시지 내용 유효성 오류
    5000	    200	        결제 성공
    5001	    400	        잔액 부족
    5002	    500	        결제 시스템 오류

     */



    public static ResponseDTO of(String status, Integer code, String message) {
        return new ResponseDTO(status, code, message);
    }

}
