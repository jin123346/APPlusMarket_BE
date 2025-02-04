package com.aplus.aplusmarket.controller;


import com.aplus.aplusmarket.service.ChargeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 *  2025.02.03 (월) 김민희 - 🍎 애쁠페이 [충전]
 *                         • 계좌생성, 충전, 잔액조회
 * */

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/pay")
public class ChargeController {

    private final ChargeService chargeService;

    /**
     * 1. 계좌 생성 API
     * - 요청 : 사용자 ID
     * - 응답 : 생성된 계좌번호 (65-사용자ID-1)
     * - 처리 : 계좌 중복 체크 후 → 새 계좌 생성
     */

    // 1. 계좌 생성
    public void createCharge (@RequestParam Long uid){

        // 1-1. 계좌 유무 체크
        String newAccount = chargeService.createAccount(uid);
    }


    // 충전

    // 잔액 조회


}
