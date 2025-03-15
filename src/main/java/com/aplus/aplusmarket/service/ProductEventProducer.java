package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.product.CrawlData;
import com.aplus.aplusmarket.dto.product.ProductEvent;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String EVENTTOPIC = "product-events";
    private static final String CRAWLERTOPIC = "crawler-topic";
    private static final String SEARCHTOPIC = "crawler-search-topic";


    public void sendProductEvent(ProductEvent event) {
        String eventJson = new Gson().toJson(event);
        kafkaTemplate.send(EVENTTOPIC, eventJson);
    }


    public void sendCrawlerProduct(CrawlData event) {
        String eventJson = new Gson().toJson(event);
        // Kafka 토픽으로 메시지 전송

        log.info("Kafka로 전송된 제품: {}", eventJson);
        kafkaTemplate.send(CRAWLERTOPIC, eventJson);
    }

    public void requestCrawlWithKeyword(String keyword){
        log.info("Kafka로 크롤링 요청: {}", keyword);
        kafkaTemplate.send(SEARCHTOPIC, keyword);


    }



}
