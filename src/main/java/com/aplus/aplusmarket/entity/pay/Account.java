package com.aplus.aplusmarket.entity.pay;
import lombok.*;

/*
 *  2025.02.03 (월) 김민희 - 🍎 애쁠페이 [계좌번호 테이블]
 *
 * [테이블명] : tb_account
 * [계좌번호 생성 규칙]
 * • [은행코드 65(고정값)]-[사용자 ID]-[계좌번호 (1부터 증가)]
 * • 예시: 65-12345-1
 * */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Account {
    private String id;  // 고유 식별자 (AUTO INCREMENT)
    private String uid; // 사용자 ID (FK)
    private String account;  // 계좌번호
    private Long payBalance; // 잔액
    private String createdAt;  // 생성일시
}
