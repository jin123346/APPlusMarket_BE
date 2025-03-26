package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductListRequestDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductModifyRequestDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductRequestDTO;
import com.aplus.aplusmarket.dto.product.response.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.service.ProductService;
import com.aplus.aplusmarket.service.WishAndRecentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/*
 * packageName    : com/aplus/aplusmarket/controller/ProductController.java
 * fileName       : ProductController.java
 * author         : 하진희
 * date           : 2024/02/05
 * description    : product 컨트롤러
 *
 * =============================================================
 * DATE           AUTHOR             NOTE
 * -------------------------------------------------------------
 * 2025-02-10     이도영     주석 추가
 * 2025-02-15     하진희     판매중상품, 완료상품, 숨김상품 컨트롤러 추가
 * 2025-02-20     하진희     상품 수정
 * 2025-02-25     하진희     상품상세보기 수정, 관심상품 처리
 * 2025-02-26     하진희     관심상품 처리
 *
 */

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final WishAndRecentService wishAndRecentService;

    //선택한 상품 정보 가지고 오기
    @GetMapping("/{id}")
    public ResponseDTO selectProductById(@PathVariable(value = "id") String id,@RequestParam(required = false) Long userId) {
        // 최근본 상품에 등록하는 로직 필요

        return productService.selectProductById(id,userId);
    }

    //상품 등록
    @PostMapping("/insert")
    public ResponseEntity insertProduct(
            @ModelAttribute ProductRequestDTO productRequestDTO,
            @RequestPart("images") List<MultipartFile> images,
            HttpServletRequest request){
        log.info("images : "+ images.size());
        String clientIp = request.getRemoteAddr();
        productRequestDTO.setRegisterIp(clientIp);
        ResponseDTO responseDTO = productService.insertProduct(productRequestDTO, images);
        return ResponseEntity.ok().body(responseDTO);
    }


    //상품 페이징 처리 (메인 홈 화면만 출력되고 있습니다)
    @GetMapping("/listpage")
    public ResponseDTO  getProducts(
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "6") int pageSize,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String keyword){

        if(keyword == null){
            keyword = "";
        }

        if(brand == null){
            brand = "";
        }
        log.info("요청한 리스트  keyword : {} , brand :{}", keyword, brand);

        return productService.selectProductsByPage(page,pageSize,keyword,brand);
    }


    @GetMapping("/on-sale")
    public ResponseEntity selectProductForSelling(@RequestBody ProductListRequestDTO productListRequestDTO){
        return ResponseEntity.ok().body(productService.selectProductByIdForSelling(productListRequestDTO));
    }

    @GetMapping("/completed")
    public ResponseEntity selectProductForCompleted(@RequestBody ProductListRequestDTO productListRequestDTO){
        return ResponseEntity.ok().body(productService.selectProductByIdForCompleted(productListRequestDTO));
    }

    @PutMapping("/reload/{productId}")
    public ResponseEntity reloadProduct(@PathVariable(value = "productId") Long productId){
        return ResponseEntity.ok().body(productService.reloadProduct(productId));
    }

    @PutMapping("/{productId}/{status}")
    public ResponseEntity updateStatusForProduct(
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "status") String status){
        return ResponseEntity.ok().body(productService.updateStatus(productId,status));
    }

    @GetMapping("/modify/{productId}/{userId}")
    public ResponseEntity modifyProduct(
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "userId") Long userId,HttpServletRequest request
            ){

        Long id =(Long)request.getAttribute("id");
        ResponseDTO responseDTO;
        if(id == null || userId != id ){
             responseDTO = ResponseDTO.error(ResponseCode.NOT_AUTHORITY);
            return ResponseEntity.status(responseDTO.getHttpCode()).body(responseDTO);

        }

         responseDTO = productService.selectProductForModify(productId,userId);
        log.info("조회 결과 {}",responseDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/modify/{productId}/{userId}")
    public ResponseEntity updateProductForModify( @RequestPart("data") String jsonData,
                                                  @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "userId") Long userId,HttpServletRequest request){

        log.info("🔥 받은 jsonData = {}", jsonData);

        Long id =(Long)request.getAttribute("id");
        ResponseDTO responseDTO;
        if(id == null || userId != id ){
            responseDTO = ResponseDTO.error(ResponseCode.NOT_AUTHORITY);
            return ResponseEntity.status(responseDTO.getHttpCode()).body(responseDTO);
        }

        ObjectMapper mapper = new ObjectMapper();
        ProductModifyRequestDTO requestDTO;
        try {
            requestDTO = mapper.readValue(jsonData, ProductModifyRequestDTO.class);

            if(requestDTO.getId() == null){requestDTO.setId(productId);}
            String clientIp = request.getRemoteAddr();
            requestDTO.setRegisterIp(clientIp);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON data");
        }
        log.info("수정할 내용 : {} ",requestDTO);
        requestDTO.setNewImages(newImages);




        return ResponseEntity.ok().body(productService.updateProduct(requestDTO));
    }

    @PutMapping("/wish/{productId}/{userId}")
    public ResponseEntity updateWish(@PathVariable(value="productId") Long productId,@PathVariable(value="userId") Long userId){
        log.info("관심상품 설정 : {} ,{} ",productId,userId);
        ResponseDTO responseDTO = wishAndRecentService.updateWishList(productId,userId);
        return ResponseEntity.ok().body(responseDTO);
    }


    @GetMapping("/wish/list")
    public ResponseEntity getWishList(HttpServletRequest request){
        Long userId = (Long)request.getAttribute("id");
        log.info("userid : {}",userId);

        return ResponseEntity.ok().body(wishAndRecentService.selectWishLit(userId));
    }

    @DeleteMapping("/delete/{productId}/{userId}")
    public ResponseEntity deleteProduct(@PathVariable(value="productId") Long productId,@PathVariable(value="userId") Long userId,HttpServletRequest request){
        Long requestId = (Long)request.getAttribute("id");
        log.info("userid : {}",requestId);
        if(requestId == null || userId != requestId){
            return ResponseEntity.status(ResponseCode.NOT_AUTHORITY.getHttpStatus()).body(ResponseCode.NOT_AUTHORITY);
        }

        log.info("관심상품 설정 : {} ,{} ",productId,userId);
        ResponseDTO responseDTO = productService.deleteProduct(productId,userId);
        return ResponseEntity.ok().body(responseDTO);
    }
}
