package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.document.Products;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.CrawlData;
import com.aplus.aplusmarket.entity.Brand;
import com.aplus.aplusmarket.entity.Category;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.product.CategoryMapper;
import com.aplus.aplusmarket.repository.ProductsRepository;
import com.aplus.aplusmarket.util.SamsungCategoryMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Document;
import org.bson.Document;
//import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


/*
 2025.02.12
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class SamsungCrawlerService {
    private final ProductsRepository productsRepository;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://sribsrch.ecom.samsung.com/estoresearch-api/v1/scom/sec/search")
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MongoTemplate mongoTemplate;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final ProductEventProducer productEventProducer;
    private final CrawlStatusService crawlStatusService;


    @Scheduled(cron = "0 0 0 * * Mon")
    public void crawlSamsungProducts() {
        for (String url : SamsungCategoryMapper.CATEGORY_CURRENT_MAP.keySet()) {
            log.info("크롤링 할 페이지 : {}",url);
            samsungList(url);
        }
    }


    private void scrapeProducts(String url,WebDriver driver) {
        try {
            Long categoryId = SamsungCategoryMapper.CATEGORY_MAP.get(url);
            String categoryName = SamsungCategoryMapper.CATEGORY_NAMES.get(categoryId);
            Long parentId = SamsungCategoryMapper.CATEGORY_PARENT_MAP.get(categoryId);

            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            List<WebElement> products = driver.findElements(By.className(".list-product"));
            Brand samsungBrand = new Brand(1L, "삼성");
            Category category = new Category(categoryId, categoryName,parentId);
            log.info("브랜드 : {} , category : {}",samsungBrand,category);
            List<String> productList = new ArrayList<>();


            for (WebElement product : products) {
                String name = product.findElements(By.className(".item .prd-name")).get(0).getText();
                String productCode = product.findElements(By.className(".item .prd-num")).get(0).getText();
                String price = product.findElements(By.className(".card-price .price-detail .pic em")).get(0).getText()
                        + product.findElements(By.className(".pic .unit")).get(0).getText();
               // String productUrl = product.findElements("a").attr("href");

              //  log.info("제품 내역 : {} , {} ,{} ,{}",name,productCode,price,productUrl);

                Products p = new Products();
                p.setBrand(samsungBrand);
                p.setCategory(category);
                p.setName(name);
                p.setProductCode(productCode);
                p.setOriginalPrice(Double.parseDouble(price));
               // p.setProductUrl(productUrl);



                productsRepository.save(p);
                log.info("저장된 제품 : {}",p);
            }

            System.out.println("카테고리 [" + categoryName + "] 크롤링 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void samsungList(String url){
        try {

            Long categoryId = SamsungCategoryMapper.CATEGORY_CURRENT_MAP.get(url);
            String categoryName = SamsungCategoryMapper.CATEGORY_NAMES.get(categoryId);
            Long parentId = SamsungCategoryMapper.CATEGORY_PARENT_MAP.get(categoryId);

            Brand samsungBrand = new Brand(1L, "삼성");
            Category category = new Category(categoryId, categoryName,parentId);
            Connection.Response response = Jsoup.connect(url)
                    .ignoreContentType(true)  // JSON 응답을 허용
                    .header("User-Agent", "Mozilla/5.0") // 웹 브라우저처럼 요청
                    .execute();
            String json = response.body();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray products = jsonObject.getJSONArray("products"); // API 응답 구조 확인 필요

            for (int i = 0; i < products.length(); i++) {
                JSONObject product = products.getJSONObject(i);
                String name = product.getString("goodsNm"); // 제품명
                String modelCode = product.getString("mdlCode"); // 제품코드(색상포함 코드인듯)
                String mdlNm = product.getString("mdlNm"); // 제품코드
                String priceStr = product.getString("priceStr");
                String goodsId = product.getString("goodsId");
                String[] priceDat = priceStr.split("\\|");
                int size = priceDat.length;
                String originalPrice = priceDat[size-2];
                String salePrice = priceDat[size-1];
                String productUrl = "https://www.samsung.com/sec" + product.getString("goodsDetailUrl"); // 상세 페이지 URL


                CrawlData crawlData = CrawlData.builder()
                        .categoryName(categoryName)
                        .brandName(samsungBrand.getName())
                        .mdlNm(mdlNm)
                        .modelCode(modelCode)
                        .goodsId(goodsId)
                        .name(name)
                        .salePrice(salePrice)
                        .originalPrice(originalPrice)
                        .productUrl(productUrl)
                        .build();

                // Kafka로 전송할 JSON 객체 생성

               productEventProducer.sendCrawlerProduct(crawlData);

            }

        } catch (Exception e) {
            throw new CustomException(ResponseCode.SAMSUNG_CRAWL_FAILED);
        }

    }

    public ResponseDTO searchProductByKeyWord(String keyword){

        log.info("검색단어 : {}",keyword);
        try{
                Query query = new Query();
                List<Criteria> criteriaList = new ArrayList<>();

                String regexKeyword = ".*" + keyword + ".*";

                // name, productCode, productDetailCode 필드 검색
                criteriaList.add(Criteria.where("name").regex(regexKeyword, "i"));
                criteriaList.add(Criteria.where("productCode").regex(regexKeyword, "i"));
                criteriaList.add(Criteria.where("productDetailCode").regex(regexKeyword, "i"));


                // keywords 필드가 존재하는 경우에만 regex 검색 추가
                Criteria keywordCriteria = Criteria.where("keywords").regex(".*" + keyword + ".*", "i"); // i: 대소문자 무시


                criteriaList.add(keywordCriteria);

                //  OR 조건 추가
                query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
                List<Products> products = mongoTemplate.find(query, Products.class);
                log.info("검색 결과 : {}개", products.size());

                if(!products.isEmpty()){
                    log.info("첫번째 검색에서 가져옴 {}",products);
                    return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_SUCCESS,products);
                }



                productEventProducer.requestCrawlWithKeyword(keyword);
                crawlStatusService.setCrawling(keyword);

                return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_IN_PROGRESS);

    //            Mono<List<Products>> lists = searchSamsungProducts(keyword,10);
    //
    //            if(lists.blockOptional().isEmpty()){
    //                return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_NOT_FOUND);
    //            }
    //
    //            products = mongoTemplate.find(query, Products.class);
    //            if (!products.isEmpty()) {
    //                return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_SUCCESS,products);
    //            }


        }catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException(ResponseCode.SAMSUNG_SEARCH_FAILED);

        }

      //  return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_NOT_FOUND);

    }

    public ResponseDTO searchCheckCrawl(String keyword) {

        log.info("검색단어 : {}", keyword);
        try {

            Query query = new Query();
            List<Criteria> criteriaList = new ArrayList<>();

            String regexKeyword = ".*" + keyword + ".*";

            // name, productCode, productDetailCode 필드 검색
            criteriaList.add(Criteria.where("name").regex(regexKeyword, "i"));
            criteriaList.add(Criteria.where("productCode").regex(regexKeyword, "i"));
            criteriaList.add(Criteria.where("productDetailCode").regex(regexKeyword, "i"));


            // keywords 필드가 존재하는 경우에만 regex 검색 추가
            Criteria keywordCriteria = Criteria.where("keywords").regex(".*" + keyword + ".*", "i"); // i: 대소문자 무시


            criteriaList.add(keywordCriteria);

            //  OR 조건 추가
            query.addCriteria(new Criteria().orOperator(criteriaList.toArray(new Criteria[0])));
            List<Products> products = mongoTemplate.find(query, Products.class);
            log.info("크롤링 후 체크 검색 결과 : {}개", products.size());

            if (!products.isEmpty()) {
                log.info("두번째 검색에서 가져옴 {}", products);
                return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_SUCCESS, products);
            }

            return ResponseDTO.success(ResponseCode.SAMSUNG_SEARCH_NOT_FOUND);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //삼성 api 요청하기

    public Mono<List<Products>> searchSamsungProducts(String keyword, int requestCount) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("clientCode", "b2c");
        requestBody.put("storeID", 1);
        requestBody.put("countryCode", "sec");
        requestBody.put("startIndex", 0);
        requestBody.put("requestCount", requestCount);
        requestBody.put("clientName", "scom");
        requestBody.put("projection", Arrays.asList("*"));
        requestBody.put("keyword", keyword);
        requestBody.put("siteCd", "sec");
        requestBody.put("version", "v2");
        requestBody.put("firstSearchYN", true);

        return webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .switchIfEmpty(Mono.error(new RuntimeException("Samsung API 응답이 비어 있습니다!")))
                .flatMap(responseBody -> processSamsungResponse(responseBody,keyword));
    }

    private Mono<List<Products>> processSamsungResponse(String responseBody,String keyword) {
        return Mono.fromCallable(() -> {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode searchResults = rootNode.path("searchResults");
            Brand samsungBrand = new Brand(1L, "삼성");

            Document brandDoc = new Document()
                    .append("_id", samsungBrand.getId())
                    .append("name", samsungBrand.getName());


            List<Products> productsList = new ArrayList<>();
            List<WriteModel<Document>> bulkOperations = new ArrayList<>(); // 올바른 Document 타입 사용

            for (JsonNode product : searchResults) {
                String categoryName = product.path("category").asText(); // ✅ 문자열로 저장
                Category category = Category.builder()
                        .categoryName(categoryName)
                        .build();
                Document categoryDoc = new Document()
                        .append("categoryName", categoryName);

                Optional<Category> opt = categoryMapper.selectCategoryByName(categoryName);
                if(opt.isPresent()){
                    category.setId(opt.get().getId());
                    category.setParentId(opt.get().getParentId());

                    categoryDoc.append("_id", category.getId());
                    categoryDoc.append("parentId", category.getParentId());
                }



                Products products = Products.builder()
                        .brand(samsungBrand)
                        .keywords(keyword)
                        .goodsId(product.path("id").asText())
                        .productCode(product.path("modelName").asText())
                        .productDetailCode(product.path("modelCode").asText())
                        .name(product.path("productDisplayName").asText())
                        .originalPrice(product.path("msrp_price").asDouble())
                        .finalPrice(product.path("sale_price").asDouble())
                        .build();
                productsList.add(products);

                // Query 대신 Bson 필터 사용
                Bson filter = Filters.eq("productDetailCode", products.getProductDetailCode());

                // MongoDB에 저장할 Document 객체 생성
                Document document = new Document()
                        .append("productDetailCode", products.getProductDetailCode())
                        .append("productCode", products.getProductCode())
                        .append("name", products.getName())
                        .append("keywords", products.getKeywords())
                        .append("category",categoryDoc)
                        .append("goodsId",products.getGoodsId())
                        .append("originalPrice", products.getOriginalPrice())
                        .append("finalPrice", products.getFinalPrice())
                        .append("brand", brandDoc);

                // Update 문서 생성
                Bson update = Updates.combine(
                        Updates.set("productDetailCode", products.getProductDetailCode()),
                        Updates.set("productCode", products.getProductCode()),
                        Updates.set("name", products.getName()),
                        Updates.set("keywords",keyword),
                        Updates.set("originalPrice", products.getOriginalPrice()),
                        Updates.set("finalPrice", products.getFinalPrice()),
                        Updates.set("goodsId", products.getGoodsId()),
                        Updates.set("category", categoryDoc),
                        Updates.set("brand",brandDoc)
                );

                // MongoDB의 UpdateOneModel 적용 (Upsert)
                bulkOperations.add(new UpdateOneModel<>(filter, update, new UpdateOptions().upsert(true)));
            }

            // MongoDB `bulkWrite` 실행 (Document로 변환)
            if (!bulkOperations.isEmpty()) {
                // MongoCollection<Document> 가져오기
                MongoCollection collection = mongoTemplate.getDb().getCollection("products");
                collection.bulkWrite(bulkOperations); // 강제 캐스팅 필요 없음
                log.info(" Batch Upsert 완료 - {}", productsList);
            }


            return productsList;
        }).subscribeOn(Schedulers.boundedElastic());
    }




//    public List<Products> searchSamsungProducts(String keyword, int requestCount) {
//        try {
//            Map<String, Object> requestBody = new HashMap<>();
//            requestBody.put("clientCode", "b2c");
//            requestBody.put("storeID", 1);
//            requestBody.put("countryCode", "sec");
//            requestBody.put("startIndex", 0);
//            requestBody.put("requestCount", requestCount);
//            requestBody.put("clientName", "scom");
//            requestBody.put("projection", Arrays.asList("*"));
//            requestBody.put("keyword", keyword);
//            requestBody.put("siteCd", "sec");
//            requestBody.put("version", "v2");
//            requestBody.put("firstSearchYN", true);
//
//            Mono<String> response = webClient.post()
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .bodyValue(requestBody)
//                    .retrieve()
//                    .bodyToMono(String.class);
//
//            String responseBody = response.block();
//            if (responseBody == null) {
//                log.error("API 응답이 비어 있습니다.");
//                return Collections.emptyList();
//            }
//
//            JsonNode rootNode = objectMapper.readTree(responseBody);
//            JsonNode searchResults = rootNode.path("searchResults");
//            Brand samsungBrand = new Brand(1L, "삼성");
//
//            List<Products> productsList = new ArrayList<>();
//            for (JsonNode product : searchResults) {
//
//                //TODO: 카테고리 조회하는 로직 추가
//                String category_name = product.path("category").asText();
//
//
//                Products products = Products.builder()
//                        .brand(samsungBrand)
//                        .goodsId(product.path("id").asText())
//                        .productCode(product.path("modelName").asText())
//                        .productDetailCode(product.path("modelCode").asText())
//                        .name(product.path("productDisplayName").asText())
//                        .originalPrice(product.path("msrp_price").asDouble())
//                        .finalPrice(product.path("sale_price").asDouble())
//                        .build();
//
//                productsList.add(products);
//
//
//                Query query = new Query();
//                query.addCriteria(Criteria.where("productDetailCode").is(products.getProductDetailCode()));
//
//                Update update = new Update()
//                        .set("productDetailCode",products.getProductDetailCode())
//                        .set("productCode",products.getProductDetailCode())
//                        .set("name", products.getName())
//                        .set("originalPrice", products.getOriginalPrice())
//                        .set("finalPrice", products.getFinalPrice())
//                        .set("brand", products.getBrand())
//                        .set("category", products.getCategory());
//
//                // upsert 수행 (조건에 맞는 문서가 없으면 새로 삽입)
//                mongoTemplate.upsert(query, update, Products.class);
//                log.info("Upsert 완료 - productCode: {}", products.getProductCode());
//
//                //productsRepository.save(products);
//
//
//            }
//
//            return productsList;
//
//        } catch (Exception e) {
//            log.error("API 요청 중 오류 발생", e);
//            return Collections.emptyList();
//
//        }
//    }


}
