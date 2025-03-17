package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.document.Products;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.CrawlData;
import com.aplus.aplusmarket.dto.product.ProductEvent;
import com.aplus.aplusmarket.entity.Brand;
import com.aplus.aplusmarket.entity.Category;
import com.aplus.aplusmarket.entity.NotificationItem;
import com.aplus.aplusmarket.entity.WishList;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.product.CategoryMapper;
import com.aplus.aplusmarket.mapper.product.NotificationItemMapper;
import com.aplus.aplusmarket.mapper.product.WishListMapper;
import com.aplus.aplusmarket.repository.ProductsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.apache.kafka.clients.consumer.Consumer;
import reactor.core.publisher.Mono;
import org.springframework.kafka.support.Acknowledgment;


import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaNotificationConsumer {

    private final NotificationService notificationService;
    private final WishListMapper wishListMapper;
    private final NotificationItemMapper notificationItemMapper;
    private final SamsungCrawlerService samsungCrawlerService;
    private final ProductsRepository productsRepository;
    private final CategoryMapper categoryMapper;
    private final CrawlStatusService crawlStatusService;


    @KafkaListener(topics = "product-events",groupId = "product-group",containerFactory = "kafkaListenerContainerFactory" )
    public void productEventConsume(String message,Acknowledgment acknowledgment){
        try{
            ProductEvent event = new Gson().fromJson(message, ProductEvent.class);
            log.info("Receive Product Event : {}", event);

            if("PRICE_UPDATED".equals(event.getEventType())){
                List<WishList> interestingUsers = wishListMapper.selectByProductId(event.getProductId());

                if (interestingUsers.isEmpty()) {
                    log.info("관심 있는 유저 없음. 알림 전송 생략.");
                    return;
                }


                List<NotificationItem> notificationItems = new ArrayList<>();
                log.info("관심 있는 유저 목록 : {}",interestingUsers);
                Set<Long> processedUserIds = new HashSet<>();

                for (WishList wishList : interestingUsers) {

                    if (!processedUserIds.contains(wishList.getUserId())) {
                        log.info("관심 있는 유저 : {}",wishList);

                        NotificationItem item = NotificationItem.builder()
                                .userId(wishList.getUserId())
                                .productId(event.getProductId())
                                .message(event.getMessage())
                                .eventType(event.getEventType())
                                .isRead(false)
                                .isDeleted(false)
                                .build();

                        log.info("알림 insert   : {}", item);
                        notificationItemMapper.insertNotificationItem(item);
                        notificationItems.add(item);

                        processedUserIds.add(wishList.getUserId()); //  중복 방지
                    }
                }

                notificationService.sendNotificationBatch( notificationItems);


            }
           acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Kafka 메시지 처리 중 오류 발생: {}", e.getMessage());
        }




    }

    @KafkaListener(topics = "crawler-topic", groupId = "product-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);

        // JSON 데이터를 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CrawlData data = objectMapper.readValue(message, CrawlData.class);
            Optional<Products> existingData = productsRepository.findByProductDetailCode(data.getModelCode());

            if (existingData.isPresent()) {
                log.info("Duplicate product found, update:  " + existingData.get().getName());
                                    // 기존 제품 업데이트
                    Products p = existingData.get();
                    p.setOriginalPrice(Double.parseDouble(data.getOriginalPrice()));
                    p.setFinalPrice(Double.parseDouble(data.getSalePrice()));
                    p.setProductUrl(data.getProductUrl());
                    p.setName(data.getName());
                    productsRepository.save(p);
                    log.info("업데이트된 제품 : {}", p);

            } else {

                Brand samsungBrand = new Brand(1L, "삼성");
                Optional<Category> opt = categoryMapper.selectCategoryByName(data.getCategoryName());
                Category category = Category.builder()
                        .categoryName(data.getCategoryName())
                        .build();
                if (opt.isPresent()) {
                    category  = opt.get();
                }

                Products savedProduct = Products.builder()
                            .category(category)
                            .brand(samsungBrand)
                            .originalPrice(Double.parseDouble(data.getOriginalPrice()))
                            .goodsId(data.getGoodsId())
                            .finalPrice(Double.parseDouble(data.getSalePrice()))
                            .productCode(data.getMdlNm())
                            .productDetailCode(data.getModelCode())
                            .name(data.getName())
                            .productUrl(data.getProductUrl())
                            .build();
                    productsRepository.save(savedProduct);
                    System.out.println("제품명: " + data.getName() + ", 가격: " + data.getOriginalPrice() + "원, 링크: " + data.getProductUrl());
                productsRepository.save(savedProduct); // 중복이 아니라면 DB에 저장
                log.info("Saved to DB: " + savedProduct);
            }
        }catch (JsonProcessingException e) {
            log.error("JSON 파싱 오류 발생: {}", e.getMessage());
        } catch (NumberFormatException e) {
            log.error("숫자 변환 오류 발생: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Kafka Consumer 처리 중 예외 발생", e);
            throw new CustomException(ResponseCode.KAFKA_SEARCH_IN_ERROR); // **필요할 경우 CustomException 던짐**
        }
    }



    @KafkaListener(topics = "crawler-search-topic", groupId = "product-group")
    public void searchConsume(String message) {
        System.out.println("Received message: " + message);

        // JSON 데이터를 Java 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();

            samsungCrawlerService.searchSamsungProducts(message, 10)
                    .doOnSuccess(products -> {
                        log.info("검색 완료: {}", products.size());
                        crawlStatusService.setCompleted(message); // ✅ Mono가 완료된 후 실행
                    })
                    .doOnError(error -> {
                        log.error("Samsung 검색 실패!", error);
                        throw new CustomException(ResponseCode.KAFKA_SEARCH_IN_ERROR);

                    })
                    .subscribe(); // ✅ subscribe() 호출해야 Mono 실행됨!



    }




}


