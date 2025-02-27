package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import com.aplus.aplusmarket.entity.WishList;
import com.aplus.aplusmarket.mapper.product.WishListMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

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
    private static final String KEY_GUEST_PREFIX = "guest:";


    //관심상품 처리 로직
    @Transactional
    public ResponseDTO updateWishList(Long productId, Long userId){
        log.info("관심상품 처리 로직 시작");
        try{
            long wishListId = wishListMapper.selectWishList(productId,userId);
            log.info("기존 관심상품 유무 체크 완료");
            if(wishListId > 0){

                wishListMapper.deleteById(wishListId);
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

        try{
            String key;
            if(cardDTO.getUserId() != null){
                key = KEY_RECENT_PREFIX+cardDTO.getUserId();
            }else{
                key = KEY_GUEST_PREFIX+cardDTO.getTmpUserId();
            }

            //기존 리스트 가져오기
            double score = System.currentTimeMillis() / 1000.0;

            redisTemplate.opsForZSet().add(key,cardDTO,score);
            redisTemplate.opsForList().set(key, 0,10);

            redisTemplate.expire(key, Duration.ofDays(2));

            return ResponseDTO.of("success",2038,"최근봉상품 등록");
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }




    }

    public DataResponseDTO getRecentProducts(Long userId) {
        String key = KEY_RECENT_PREFIX+userId;
        Set<Object> productLists =  redisTemplate.opsForZSet().range(key,0,-1);


        return DataResponseDTO.of(productLists,2039,"최근본 상품 리스트");
    }

    public void mergeGuestDataToUser(String tempUserId, Long userId) {
        String guestKey =KEY_GUEST_PREFIX + tempUserId;
        String userKey = KEY_RECENT_PREFIX + userId;

        // 1. 비회원 데이터 가져오기
        Set<Object> guestData = redisTemplate.opsForZSet().range(guestKey, 0, -1);

        // 2. 회원 데이터 가져오기
        Set<Object> userData = redisTemplate.opsForZSet().range(userKey, 0, -1);

        redisTemplate.opsForZSet().unionAndStore(userKey, guestKey,userKey);

        // 7일 동안 유지되도록 설정
        redisTemplate.expire(userKey, Duration.ofDays(7));
        // 5. 비회원 데이터 삭제
        redisTemplate.delete(guestKey);
    }


}
