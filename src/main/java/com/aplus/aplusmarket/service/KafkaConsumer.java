package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.repository.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaConsumer {


    private final ProductsRepository productsRepository;
    private final ObjectMapper objectMapper;


}
