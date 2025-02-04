package com.aplus.aplusmarket.entity.pay;

import lombok.*;

/*
 *  2025.02.03 (월) 김민희 - 🍎 애쁠페이 [거래 내역]
 *
 *  [테이블명] : tb_account_history
 *  [거래유형]
 *  • Charge: 충전
 *  • Pay: 결제
 *  • Transfer: 이체
 *  • Withdraw: 출금 (w)
 *  • Deposit: 입금 (d)
 * */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 자동 생성
@Data
public class AccoutHistory {

    private Long id;                 // 거래 내역 ID
    private Long userId;             // 사용자 ID
    private Long amount;             // 거래 금액
    private Long wAccountId;         // 출금 계좌 ID (NULL이면 충전)
    private Long dAccountId;         // 입금 계좌 ID (NULL이면 출금)
    private Long wBalance;           // 출금 후 잔액
    private Long dBalance;           // 입금 후 잔액
    private String transactionType;  // 거래 유형 (CHARGE, PAYMENT, TRANSFER)
    private String status;           // 거래 상태 (SUCCESS, FAIL)
    private String createdAt;        // 거래 일시
}
