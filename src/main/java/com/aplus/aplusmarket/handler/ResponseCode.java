package com.aplus.aplusmarket.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ResponseCode {

    // 로그인 관련
    NOT_AUTHORITY(1401,HttpStatus.UNAUTHORIZED,"허가되지 않은 유저입니다."),
    NO_TOKEN_IN_HERE(1402,HttpStatus.NO_CONTENT,"토큰이 존재하지 않습니다."),
    NO_INPUT_DATA(1403,HttpStatus.NO_CONTENT,"Input 값이 없음."),
    LOGIN_SUCCESS(1000, HttpStatus.OK, "로그인 성공"),
    LOGIN_PASSWORD_ERROR(1001, HttpStatus.UNAUTHORIZED, "비밀번호 오류"),
    LOGIN_ACCOUNT_LOCKED(1002, HttpStatus.FORBIDDEN, "계정 잠김"),
    LOGIN_ACCOUNT_NOT_FOUND(1003, HttpStatus.NOT_FOUND, "존재하지 않는 계정"),
    LOGIN_REFRESH_TOKEN_NOT_FOUND(1005, HttpStatus.UNAUTHORIZED, "refreshToken이 존재하지 않음"),
    LOGIN_REQUIRED(1006, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    LOGIN_REFRESH_TOKEN_SUCCESS(1219, HttpStatus.OK, "refresh 성공"),
    LOGIN_REFRESH_TOKEN_NOT_VALIDATED(1220, HttpStatus.UNAUTHORIZED, "refresh 유효성 없음"),
    LOGIN_REFRESH_TOKEN_INTERNAL_ERROR(1221, HttpStatus.UNAUTHORIZED, "refreshToken 설정 중 오류 발생"),

    // 회원 관련
    MEMBER_FIND_ID_SUCCESS(1006, HttpStatus.OK, "아이디 찾기 성공"),
    MEMBER_FIND_ID_FAIL(1007, HttpStatus.NOT_FOUND, "아이디 찾기 실패"),
    MEMBER_FIND_PASS_SUCCESS(1008, HttpStatus.OK, "비밀번호 찾기 성공"),
    MEMBER_PASS_UPDATE_SUCCESS(1009, HttpStatus.OK, "비밀번호 업데이트 성공"),
    MEMBER_FIND_PASS_FAIL(1010, HttpStatus.NOT_FOUND, "비밀번호 찾기 실패"),
    MEMBER_PASS_UPDATE_FAIL(1011, HttpStatus.NOT_FOUND, "비밀번호 업데이트 실패"),
    MEMBER_FIND_ID_NOT_FOUND(1012, HttpStatus.NOT_FOUND, "아이디 찾기 결과 없음"),
    MEMBER_FIND_PASS_NOT_FOUND(1013, HttpStatus.NOT_FOUND, "비밀번호 찾기 결과 없음"),
    MEMBER_REGISTER_SUCCESS(1100, HttpStatus.CREATED, "회원가입 성공"),
    MEMBER_EMAIL_DUPLICATE(1101, HttpStatus.BAD_REQUEST, "이메일 중복"),
    MEMBER_TERMS_NOT_AGREED(1102, HttpStatus.BAD_REQUEST, "약관 미동의"),
    MEMBER_REGISTER_FAIL(1103, HttpStatus.BAD_REQUEST, "회원가입 실패"),
    MEMBER_DELETE_SUCCESS(1104, HttpStatus.OK, "회원탈퇴 성공"),
    MEMBER_DELETE_FAIL(1105, HttpStatus.NOT_FOUND, "회원탈퇴 실패"),
    MEMBER_ID_NOT_MATCH_TOKEN(1106, HttpStatus.INTERNAL_SERVER_ERROR, "회원 탈퇴 id값이 토큰과 불일치"),
    MEMBER_NOT_FOUND(1107, HttpStatus.BAD_REQUEST, "회원 조회되지 않음"),
    MEMBER_INFO_SUCCESS(1200, HttpStatus.OK, "회원정보 가져오기 성공"),
    MEMBER_INFO_FAIL(1201, HttpStatus.NOT_FOUND, "회원조회 실패"),
    MEMBER_UPDATE_SUCCESS(1202, HttpStatus.OK, "회원 수정 성공"),
    MEMBER_UPDATE_FAIL_NO_ID(1203, HttpStatus.INTERNAL_SERVER_ERROR, "회원 수정시 id 값 안들어옴"),
    MEMBER_UPDATE_FAIL(1204, HttpStatus.INTERNAL_SERVER_ERROR, "회원 수정 실패"),
    PROFILE_UPDATE_SUCCESS(1210, HttpStatus.OK, "프로필 수정 성공"),
    PROFILE_UPDATE_FAIL(1211, HttpStatus.INTERNAL_SERVER_ERROR, "프로필 수정 실패"),
    EMAIL_SEND_SUCCESS(1212, HttpStatus.OK, "이메일을 전송했습니다."),
    EMAIL_CODE_TIMEOUT(1205, HttpStatus.OK, "인증 시간이 초과되었습니다."),
    EMAIL_CODE_CORRECT(1206, HttpStatus.OK, "인증번호가 일치하지 않습니다."),
    EMAIL_CODE_NOT_CORRECT(1207, HttpStatus.OK, "이메일 인증 성공"),
    NOT_FILE(1208, HttpStatus.BAD_REQUEST, "파일이 없습니다."),
    EMAIL_ALREADY_EiXST(1209, HttpStatus.OK, "이미 등록된 이메일 입니다."),


    LOGIN_INTERNAL_SERVER_ERROR(1215, HttpStatus.NOT_FOUND, "로그인 중 에러발생"),
    MEMBER_INTERNAL_SERVER_ERROR(1216, HttpStatus.NOT_FOUND, "회원 조회 중 에러발생"),

    LOGOUT_SUCCESS(1217,HttpStatus.OK,"로그아웃 완료"),
    LOGOUT_FAILED(1218,HttpStatus.BAD_REQUEST,"로그아웃 중 실패"),

    //주소관련
    ADDRESS_FIND_SUCCESS(1311, HttpStatus.OK, "주소 찾기 성공"),
    ADDRESS_NOT_FOUND(1312,HttpStatus.NOT_FOUND,"주소 찾기 결과 없음"),
    ADDRESS_CREATE_SUCCESS(1313,HttpStatus.CREATED,"주소 등록 성공"),
    ADDRESS_CREATE_FAILED(1315,HttpStatus.BAD_REQUEST,"주소 등록 실패했습니다."),
    ADDRESS_MODIFY_SUCCESS(1316,HttpStatus.CREATED,"주소 수정 성공"),
    ADDRESS_MODIFY_FAILED(1317,HttpStatus.NOT_MODIFIED,"주소 수정 실패"),
    ADDRESS_DELETED_FAILED(1318,HttpStatus.BAD_REQUEST,"주소 삭제 실패"),
    ADDRESS_DELETED_SUCCESS(1319,HttpStatus.OK,"주소 삭제 성공"),
    ADDRESS_DELETED_NOT_AUTHORITY(1320,HttpStatus.UNAUTHORIZED,"사용자가 일치 하지 않습니다."),
    ADDRESS_INTERNAL_SERVER_ERROR(1314,HttpStatus.INTERNAL_SERVER_ERROR,"주소 조회 중 오류 "),



    // 상품 등록 관련
    PRODUCT_REGISTER_SUCCESS(2000, HttpStatus.CREATED, "상품 등록 성공"),
    PRODUCT_REGISTER_FAILED(2001, HttpStatus.BAD_REQUEST, "상품 등록 실패"),
    PRODUCT_IMAGE_NOT_FOUND(2002, HttpStatus.BAD_REQUEST, "Image가 들어오지 않음!"),

    // 상품 상세 조회 관련
    PRODUCT_DETAIL_SUCCESS(2003, HttpStatus.OK, "상품 상세 정보 조회 성공"),
    PRODUCT_DETAIL_FAILED(2004, HttpStatus.BAD_REQUEST, "상품 상세 정보 조회 실패"),
    PRODUCT_NOT_FOUND(2005, HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),

    //상품 목록 조회 관련
    PRODUCT_LIST_SUCCESS(2006, HttpStatus.OK, "상품 목록 조회 성공"),
    PRODUCT_LIST_FAILED(2007, HttpStatus.BAD_REQUEST, "상품 목록 조회 실패"),
    PRODUCT_LIST_EMPTY(2008, HttpStatus.OK, "조회된 상품이 없습니다."),

    // 판매 중인 상품 조회 관련
    PRODUCT_SELLING_LIST_SUCCESS(2009, HttpStatus.OK, "판매 중인 상품 조회 성공"),
    PRODUCT_SELLING_LIST_EMPTY(2010, HttpStatus.OK, "판매 중인 상품이 없습니다."),

    // 구매 완료된 상품 조회 관련
    PRODUCT_PURCHASED_LIST_SUCCESS(2011, HttpStatus.OK, "구매 완료된 상품 조회 성공"),
    PRODUCT_PURCHASED_LIST_EMPTY(2012, HttpStatus.OK, "구매 완료된 상품이 없습니다."),

    // 상품 삭제 관련
    PRODUCT_DELETE_SUCCESS(2013, HttpStatus.OK, "상품 삭제 성공"),
    PRODUCT_DELETE_FAILED(2014, HttpStatus.BAD_REQUEST, "상품 삭제 실패"),
    PRODUCT_DELETE_NOT_FOUND(2015, HttpStatus.NOT_FOUND, "삭제할 상품을 찾을 수 없습니다."),

    // 상품 업데이트 관련
    PRODUCT_UPDATE_SUCCESS(2020, HttpStatus.OK, "상품 수정 완료"),
    PRODUCT_UPDATE_FAILED(2021, HttpStatus.INTERNAL_SERVER_ERROR, "상품 수정 실패"),
    PRODUCT_UPDATE_PERMISSION_DENIED(2022, HttpStatus.FORBIDDEN, "상품 수정 권한이 없습니다."),

    // 상품 끌어올리기 관련
    PRODUCT_RELOAD_SUCCESS(2030, HttpStatus.OK, "상품 끌어올리기 성공"),
    PRODUCT_RELOAD_FAILED(2031, HttpStatus.BAD_REQUEST, "상품 끌어올리기 실패"),

    PRODUCT_STATUS_UPDATE_SUCCESS(2040, HttpStatus.OK, "상품 상태 업데이트 성공"),
    PRODUCT_STATUS_UPDATE_FAILED(2041, HttpStatus.BAD_REQUEST, "상품 상태 업데이트 실패"),

    PRODUCT_WISHLIST_COUNT_SUCCESS(2050, HttpStatus.OK, "찜한 상품 개수 조회 성공"),
    PRODUCT_WISHLIST_COUNT_FAILED(2051, HttpStatus.BAD_REQUEST, "찜한 상품 개수 조회 실패"),
    // 관심상품 관련
    WISH_ADD_SUCCESS(3035, HttpStatus.OK, "관심상품 등록 성공"),
    WISH_REMOVE_SUCCESS(3032, HttpStatus.OK, "관심상품 해제 완료"),
    WISH_UPDATE_FAILED(2034, HttpStatus.BAD_REQUEST, "관심상품 업데이트 실패"),
    WISH_UPDATE_SUCCESS(2035, HttpStatus.OK, "관심상품 업데이트 성공"),
    WISH_PROCESS_FAILED(2033, HttpStatus.INTERNAL_SERVER_ERROR, "관심상품 처리 중 오류 발생"),
    WISH_LIST_SUCCESS(2036, HttpStatus.OK, "관심상품 조회 성공"),
    WISH_LIST_FAILED(2035, HttpStatus.INTERNAL_SERVER_ERROR, "관심상품 가져오기 실패"),

    //최근 본 상품 관련
    RECENT_PRODUCT_ADD_SUCCESS(2038, HttpStatus.OK, "최근 본 상품 등록 성공"),
    RECENT_PRODUCT_ADD_FAILED(2037, HttpStatus.BAD_REQUEST, "최근 본 상품 등록 실패"),
    RECENT_PRODUCT_LIST_SUCCESS(2039, HttpStatus.OK, "최근 본 상품 조회 성공"),
    RECENT_PRODUCT_LIST_FAILED(2040, HttpStatus.INTERNAL_SERVER_ERROR, "최근 본 상품 조회 실패"),
    RECENT_PRODUCT_MERGE_SUCCESS(2041, HttpStatus.OK, "비회원 최근 본 상품 데이터 회원 계정으로 병합 성공"),
    RECENT_PRODUCT_MERGE_FAILED(2042, HttpStatus.INTERNAL_SERVER_ERROR, "비회원 최근 본 상품 병합 중 오류 발생"),

    INTERNAL_SERVER_ERROR(5000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류 발생"),

    CATEGORY_FIND_SUCCESS(2100,HttpStatus.OK,"카테고리 조회 성공"),
    CATEGORY_FIND_FAILED(2101,HttpStatus.BAD_REQUEST,"카테고리 조회 실패"),

    // 거래 관련
    TRANSACTION_SUCCESS(3000, HttpStatus.OK, "거래 성공"),
    TRANSACTION_INSUFFICIENT_BALANCE(3001, HttpStatus.BAD_REQUEST, "잔액 부족"),
    TRANSACTION_LIMIT(3002, HttpStatus.BAD_REQUEST, "거래 제한"),

    // 채팅 관련
    CHAT_RETRIEVE_SUCCESS(4000, HttpStatus.OK, "채팅 - 조회 성공"),
    CHAT_RETRIEVE_FAIL(4001, HttpStatus.BAD_REQUEST, "채팅 - 조회 오류"),
    CHAT_UPDATE_SUCCESS(4002, HttpStatus.OK, "채팅 - 업데이트 완료"),
    CHAT_UPDATE_FAILED(4003, HttpStatus.BAD_REQUEST, "채팅 - 업데이트 실패"),
    CHAT_ROOM_NOT_EXIST(4004, HttpStatus.NOT_FOUND, "해당 유저의 채팅방은 존재하지 않습니다."),
    CHAT_ROOM_EXIST(4005, HttpStatus.OK, "채팅방 조회 성공"),
    CHAT_ROOM_FIND_FAILED(4007, HttpStatus.BAD_REQUEST, "채팅방 목록 조회 실패"),
    CHAT_RECENT_NOT_EXIST(4006, HttpStatus.NOT_FOUND, "최신 메시지가 있는 채팅방이 존재하지 않습니다."),

    CHAT_ROOM_DETAIL_FIND_SUCCESS(4008,HttpStatus.OK,"채팅방 상세 조회 성공"),
    CHAT_ROOM_DETAIL_FIND_FAILED(4009,HttpStatus.OK,"채팅방 상세 조회 실패"),
    CHAT_ROOM_ID_NOT_FOUND(4010,HttpStatus.NOT_FOUND,"해당 id 채팅방이 존재하지 않습니다."),
    CHAT_SOCKET_SUCCESS(4010,HttpStatus.NOT_FOUND,"Message broadcasted successfully"),

    // 채팅방 메시지 관련
    CHAT_MESSAGE_SEND_SUCCESS(4100, HttpStatus.OK, "채팅 메시지 전송 성공"),
    CHAT_MESSAGE_SEND_FAILED(4101, HttpStatus.INTERNAL_SERVER_ERROR, "채팅 메시지 전송 실패"),
    CHAT_MESSAGE_LIST_SUCCESS(4102, HttpStatus.OK, "채팅 메시지 목록 조회 성공"),
    CHAT_MESSAGE_LIST_FAILED(4103, HttpStatus.INTERNAL_SERVER_ERROR, "채팅 메시지 목록 조회 실패"),

    // 채팅방 생성 관련
    CHAT_ROOM_CREATE_SUCCESS(4200, HttpStatus.OK, "채팅방 생성 성공"),
    CHAT_ROOM_ALREADY_EXIST(4201, HttpStatus.OK, "이미 존재하는 채팅방을 반환하였습니다."),
    CHAT_ROOM_CREATE_FAILED(4202, HttpStatus.INTERNAL_SERVER_ERROR, "채팅방 생성 중 오류 발생"),
    CHAT_ROOM_ID_MISSING(4203, HttpStatus.INTERNAL_SERVER_ERROR, "저장된 채팅방 ID를 조회하지 못했습니다."),

    // 채팅방 구독 관련
    CHAT_SUBSCRIPTION_SUCCESS(4300, HttpStatus.OK, "구독할 채팅방 ID 조회 성공"),
    CHAT_SUBSCRIPTION_NOT_FOUND(4301, HttpStatus.BAD_REQUEST, "해당하는 ID로 조회되는 채팅방이 없습니다."),
    CHAT_SUBSCRIPTION_FAILED(4302, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류로 인해 채팅방 목록을 가져올 수 없습니다."),

    // 채팅방 업데이트 관련
    CHAT_UPDATE_APPOINTMENT_SUCCESS(4400, HttpStatus.OK, "Appointment 업데이트 성공"),
    CHAT_UPDATE_APPOINTMENT_FAILED(4401, HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    CHAT_UPDATE_APPOINTMENT_NO_DATA(4402, HttpStatus.BAD_REQUEST, "업데이트할 데이터가 없습니다."),
    CHAT_UPDATE_APPOINTMENT_ID_MISSING(4403, HttpStatus.BAD_REQUEST, "메시지 ID는 필수입니다."),



    // 크롤링 관련
    SAMSUNG_CRAWL_SUCCESS(3000, HttpStatus.OK, "삼성 제품 크롤링 성공"),
    SAMSUNG_CRAWL_FAILED(3001, HttpStatus.INTERNAL_SERVER_ERROR, "삼성 제품 크롤링 실패"),
    SAMSUNG_CRAWL_NO_PRODUCTS(3002, HttpStatus.OK, "크롤링된 제품이 없습니다."),

    // 검색 관련
    SAMSUNG_SEARCH_SUCCESS(3010, HttpStatus.OK, "삼성 제품 검색 성공"),
    SAMSUNG_SEARCH_IN_PROGRESS(3014, HttpStatus.OK, "삼성 제품 크롤링 중"),
    SAMSUNG_SEARCH_NOT_FOUND(3011, HttpStatus.NOT_FOUND, "검색된 제품이 없습니다."),
    SAMSUNG_SEARCH_FAILED(3012, HttpStatus.INTERNAL_SERVER_ERROR, "삼성 제품 검색 실패"),
    SAMSUNG_SEARCH_API_FAILED(3013, HttpStatus.INTERNAL_SERVER_ERROR, "삼성 API 검색 실패"),

    // API 요청 관련
    SAMSUNG_API_REQUEST_SUCCESS(3020, HttpStatus.OK, "삼성 API 요청 성공"),
    SAMSUNG_API_REQUEST_FAILED(3021, HttpStatus.INTERNAL_SERVER_ERROR, "삼성 API 요청 중 오류 발생"),
    SAMSUNG_API_RESPONSE_EMPTY(3022, HttpStatus.NO_CONTENT, "삼성 API 응답이 비어 있습니다."),

    // 데이터 저장 관련
    SAMSUNG_PRODUCT_SAVE_SUCCESS(3030, HttpStatus.OK, "삼성 제품 저장 성공"),
    SAMSUNG_PRODUCT_SAVE_FAILED(3031, HttpStatus.INTERNAL_SERVER_ERROR, "삼성 제품 저장 실패"),
    KAFKA_SEARCH_IN_ERROR(3032,HttpStatus.INTERNAL_SERVER_ERROR,"Kafka Consumer에서 예외 발생: "),

    // 결제 관련
    PAYMENT_SUCCESS(5000, HttpStatus.OK, "결제 성공"),
    PAYMENT_INSUFFICIENT_BALANCE(5001, HttpStatus.BAD_REQUEST, "잔액 부족"),
    PAYMENT_SYSTEM_ERROR(5002, HttpStatus.INTERNAL_SERVER_ERROR, "결제 시스템 오류");

    private final int code;
    private final HttpStatus httpStatus;
    private final String message;

    ResponseCode(int code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpCode() {
        return this.httpStatus.value();
    }
}
