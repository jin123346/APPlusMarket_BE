package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.document.Products;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.repository.ProductsRepository;
import com.aplus.aplusmarket.service.CrawlStatusService;
import com.aplus.aplusmarket.service.SamsungCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CrawlerController {
    private final SamsungCrawlerService samsungCrawlerService;
    private final ProductsRepository productsRepository;
    private final CrawlStatusService crawlStatusService;


    @GetMapping("/samsung/crawl")
    public String crawlSamsungProducts() {
        samsungCrawlerService.crawlSamsungProducts();
        return "삼성 제품 크롤링 완료!";
    }

    @GetMapping("/samsung/search")
    public ResponseEntity searchSamsung(@RequestParam String keyword){
        String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        System.out.println("디코딩된 검색어: " + decodedKeyword);
        boolean isCrawl = crawlStatusService.isCrawling(decodedKeyword);

        if(isCrawl){
            ResponseDTO responseDTO = ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_IN_PROGRESS);
            return ResponseEntity.ok().body(responseDTO);
        }

        ResponseDTO responseDTO =  samsungCrawlerService.searchProductByKeyWord(decodedKeyword); // 검색 개수 최대 10개

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/samsung/search/check")
    public ResponseEntity searchCheck(@RequestParam String keyword){
        String decodedKeyword = URLDecoder.decode(keyword, StandardCharsets.UTF_8);
        System.out.println("디코딩된 검색어: " + decodedKeyword);

        boolean isCrawl = crawlStatusService.isCrawling(decodedKeyword);
        if(isCrawl){
            ResponseDTO responseDTO = ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_IN_PROGRESS);
            return ResponseEntity.ok().body(responseDTO);
        }
        ResponseDTO responseDTO =  samsungCrawlerService.searchCheckCrawl(decodedKeyword); // 검색 개수 최대 10개
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/samsung/list")
    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }
}
