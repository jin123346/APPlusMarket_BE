package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    //상품 전체 가지고 오기
    @GetMapping("/list")
    public List<Product> selectAllProducts() {
        List<Product> product = productService.selectAllProducts();
        log.info("products : " + product);
        return product;
    }
    //선택한 상품 정보 가지고 오기
    @GetMapping("/{id}")
    public ResponseDTO selectProductById(@PathVariable String id) {

        return productService.selectProductById(id);
    }
    //상품 등록
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity insertProduct(
            @ModelAttribute ProductDTO productDTO,
            @RequestPart("images") List<MultipartFile> images) throws IOException {
        log.info("images : "+ images.size());
        ResponseDTO responseDTO = productService.insertProduct(productDTO, images);
        return ResponseEntity.ok().body(responseDTO);
    }

    //상품 삭제
    @DeleteMapping("/delete/{id}")
    public boolean deleteProductById(@PathVariable String id) {
        boolean check = productService.deleteProductById(id);
        return check;
    }
    //상품 페이징 처리
    @GetMapping("/listpage")
    public ResponseDTO  getProducts(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize
    ){
        
        return productService.selectProductsByPage(page,pageSize);
    }
    @PutMapping("/update")
    public boolean updateProduct(@RequestBody ProductDTO productDTO){
        boolean check = productService.updateProduct(productDTO);
        return check;
    }
}
