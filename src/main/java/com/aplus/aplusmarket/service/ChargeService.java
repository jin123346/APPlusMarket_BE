package com.aplus.aplusmarket.service;


import com.aplus.aplusmarket.mapper.pay.ChargeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
 *  2025.02.04 (화) 김민희 -
 *
 *
 * */
@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class ChargeService {

    final private ChargeMapper chargeMapper;

    public String createAccount(Long uid) {
        return null;
    }


}
