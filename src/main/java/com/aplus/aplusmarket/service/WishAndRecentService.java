package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import com.aplus.aplusmarket.entity.WishList;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
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
            Optional<Long> opt = wishListMapper.selectWishList(productId,userId);
            log.info("기존 관심상품 유무 체크 완료");
            if(opt.isPresent()){
                Long wishListId = opt.get();
                wishListMapper.deleteById(wishListId);
                log.info("기존 관심상품 삭제 => 관심상품 해제 완료");

                return ResponseDTO.success(ResponseCode.WISH_REMOVE_SUCCESS);
            }

            int result = wishListMapper.insertWishList(productId,userId);
            log.info("기존 관심상품 등록 완료");

            if(result == 0 ){
                return ResponseDTO.error(ResponseCode.WISH_UPDATE_FAILED);
            }
            return ResponseDTO.success(ResponseCode.WISH_UPDATE_SUCCESS);
        }catch (Exception e){
            throw new CustomException(ResponseCode.WISH_UPDATE_FAILED);
        }

    }


    public ResponseDTO selectWishLit(Long userId){
        try{
            List<ProductResponseCard> list = wishListMapper.productWishList(userId);

            log.info("조회 결과 : {}",list);
            return ResponseDTO.success(ResponseCode.WISH_LIST_SUCCESS,list);



        }catch (Exception e){
            throw new CustomException(ResponseCode.WISH_LIST_FAILED);
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

            return ResponseDTO.success(ResponseCode.RECENT_PRODUCT_ADD_SUCCESS);

        }catch(Exception e){
            log.error(e.getMessage());
            throw new CustomException(ResponseCode.RECENT_PRODUCT_ADD_FAILED);
        }




    }

    public ResponseDTO getRecentProducts(Long userId) {
        String key = KEY_RECENT_PREFIX+userId;
        Set<Object> productLists =  redisTemplate.opsForZSet().range(key,0,-1);

        return ResponseDTO.success(ResponseCode.RECENT_PRODUCT_LIST_SUCCESS,productLists);

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
