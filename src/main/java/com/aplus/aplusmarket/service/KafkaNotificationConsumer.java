package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.document.Products;
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


    @KafkaListener(topics = "product-events",groupId = "product-group")
    public void productEventConsume(String message){
        ProductEvent event = new Gson().fromJson(message, ProductEvent.class);
        log.info("Receive Product Event : {}", event);

        if("PRICE_UPDATED".equals(event.getEventType())){
            List<WishList> interestingUsers = wishListMapper.selectByProductId(event.getProductId());

            List<NotificationItem> notificationItems = new ArrayList<>();
            //websocket 알람
            for(WishList wishList : interestingUsers){
                NotificationItem item = NotificationItem.builder()
                        .userId(wishList.getUserId())
                        .productId(event.getProductId())
                        .message(event.getMessage())
                        .eventType(event.getEventType())
                        .isRead(false)
                        .isDeleted(false)
                        .build();
                notificationService.sendNotification(wishList.getUserId(), item);

                notificationItems.add(item);

            }

            for(NotificationItem items:notificationItems){
                notificationItemMapper.insertNotificationItem(items);
            }



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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}


