package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductListRequestDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductModifyRequestDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductRequestDTO;
import com.aplus.aplusmarket.dto.product.response.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.service.ProductService;
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
*   2025-02-10 이도영 : 주석 추가
* */
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    //상품 전체 가지고 오기 (사용하지 않습니다) getProducts로 상품 리스트 가지고 오는걸로 변경
//    @GetMapping("/list")
//    public List<Product> selectAllProducts() {
//        List<Product> product = productService.selectAllProducts();
//        log.info("products : " + product);
//        return product;
//    }
    //선택한 상품 정보 가지고 오기
    @GetMapping("/{id}")
    public ResponseDTO selectProductById(@PathVariable String id) {
        return productService.selectProductById(id);
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

    //상품 삭제(프론트 기능 없습니다 현재)
    @DeleteMapping("/delete/{id}")
    public boolean deleteProductById(@PathVariable String id) {
        boolean check = productService.deleteProductById(id);
        return check;
    }
    
    //상품 페이징 처리 (메인 홈 화면만 출력되고 있습니다)
    @GetMapping("/listpage")
    public ResponseDTO  getProducts(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize){
        return productService.selectProductsByPage(page,pageSize);
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
    public ResponseEntity reloadProduct(
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "status") String status){
        return ResponseEntity.ok().body(productService.updateStatus(productId,status));
    }

    @GetMapping("/modify/{productId}/{userId}")
    public ResponseEntity modifyProduct(
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "userId") Long userId
            ){
        return ResponseEntity.ok().body(productService.selectProductForModify(productId,userId));
    }

    @PutMapping("/modify/{productId}/{userId}")
    public ResponseEntity updateProductForModify( @RequestPart("data") String jsonData,  // ✅ @RequestBody 사용
                                                  @RequestPart(value = "newImages", required = false) List<MultipartFile> newImages,
            @PathVariable(value = "productId") Long productId,
            @PathVariable(value = "userId") Long userId,HttpServletRequest request){
        ObjectMapper mapper = new ObjectMapper();
        ProductModifyRequestDTO requestDTO;
        try {
            requestDTO = mapper.readValue(jsonData, ProductModifyRequestDTO.class);

            String clientIp = request.getRemoteAddr();
            requestDTO.setRegisterIp(clientIp);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Invalid JSON data");
        }
        log.info("수정할 내용 : {} ",requestDTO);
        requestDTO.setNewImages(newImages);




        return ResponseEntity.ok().body(productService.updateProduct(requestDTO));
    }
}
