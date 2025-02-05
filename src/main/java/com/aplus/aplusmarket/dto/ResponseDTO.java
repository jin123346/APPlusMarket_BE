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
    코드   / http status /    message
    1000	    200	        로그인 성공
    1001	    401	        비밀번호 오류
    1002	    403	        계정 잠김
    1003	    404	        존재하지 않는 계정
    1004
    1005                    refreshToken이 존재하지않음
    1009        200         로그아웃 성공
    1100	    201	        회원가입 성공
    1101	    400	        이메일 중복
    1102	    400	        약관 미동의
    1103	    201	        회원가입 실패
    2000	    201	        상품 등록 성공
    2001	    400	        상품 등록 실패
    2002        200         상품 상세 정보 조회 성공
    2003        400         상품 상세 정보 조회 실패
    2004        200         상품 목록 조회 성공
    2005        400         상품 목록 조회 실패
    3000	    200     	거래 성공
    3001	    400     	잔액 부족
    3002	    400     	거래 제한
    4000	    200	        채팅방 조회 성공
    4001	    400	        채팅방 조회 오류
    5000	    200	        결제 성공
    5001	    400	        잔액 부족
    5002	    500	        결제 시스템 오류

     */



    public static ResponseDTO of(String status, Integer code, String message) {
        return new ResponseDTO(status, code, message);
    }

}
