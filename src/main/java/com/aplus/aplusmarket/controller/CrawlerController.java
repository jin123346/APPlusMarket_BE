package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.document.Products;
import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.repository.ProductsRepository;
import com.aplus.aplusmarket.service.SamsungCrawlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CrawlerController {
    private final SamsungCrawlerService samsungCrawlerService;
    private final ProductsRepository productsRepository;


    @GetMapping("/samsung/crawl")
    public String crawlSamsungProducts() {
        samsungCrawlerService.crawlSamsungProducts();
        return "삼성 제품 크롤링 완료!";
    }

    @GetMapping("/samsung/search")
    public ResponseEntity searchSamsung(@RequestParam String keyword){
        ResponseDTO responseDTO =  samsungCrawlerService.searchProductByKeyWord(keyword); // 검색 개수 최대 10개

        if(responseDTO instanceof DataResponseDTO<?>){
            return ResponseEntity.ok().body((DataResponseDTO)responseDTO);
        }

        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/samsung/list")
    public List<Products> getAllProducts() {
        return productsRepository.findAll();
    }
}
