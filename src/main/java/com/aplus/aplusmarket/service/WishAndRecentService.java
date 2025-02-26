package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import com.aplus.aplusmarket.entity.WishList;
import com.aplus.aplusmarket.mapper.product.ProductMapper;
import com.aplus.aplusmarket.mapper.product.WishListMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/*
 * packageName    : com.aplus.aplusmarket.service/WishAndRecentService.java
 * fileName       : WishAndRecentService.java
 * author         : 하진희
 * date           : 2024/02/25
 * description    : WishAndRecentService 관심상품 및 최근본 상품 처리 서비스
 *
 * =============================================================
 * DATE           AUTHOR             NOTE
 * -------------------------------------------------------------
 * 2025-02-25       하진희            관심상품 처리 로직
 *
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class WishAndRecentService {
    private final WishListMapper wishListMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String KEY_RECENT_PREFIX = "recent:";


    //관심상품 처리 로직
    @Transactional
    public ResponseDTO updateWishList(Long productId, Long userId){
        log.info("관심상품 처리 로직 시작");
        try{
            Optional<WishList> opt = wishListMapper.selectWishList(productId,userId);
            log.info("기존 관심상품 유무 체크 완료");

            if(opt.isPresent()){
                WishList wishList  = opt.get();
                wishListMapper.deleteById(wishList.getId());
                log.info("기존 관심상품 삭제 => 관심상품 해제 완료");

                return ResponseDTO.of("Success",3032,"관심상품 해제 완료");
            }

            int result = wishListMapper.insertWishList(productId,userId);
            log.info("기존 관심상품 등록 완료");

            if(result == 0 ){
              return    ErrorResponseDTO.of(2034,"관심상품 업데이트 처리가 안됨");
            }
            return ResponseDTO.of("success",3032,"관심상품 업데이트 처리 완료");
        }catch (Exception e){
            return ErrorResponseDTO.of(2033,"관심상품 처리 중 에러 발생");
        }

    }


    public ResponseDTO selectWishLit(Long userId){
        try{
            List<ProductResponseCard> list = wishListMapper.productWishList(userId);

            log.info("조회 결과 : {}",list);
            return DataResponseDTO.of(list,2036,"관심상품 조회 성공");


        }catch (Exception e){
            return ErrorResponseDTO.of(2035,"관심상품 가져오기 실패");
        }
    }

    public ResponseDTO addRecentList(ProductCardDTO cardDTO){
        String key = KEY_RECENT_PREFIX+cardDTO.getUserId();
        return null;
    }

}
