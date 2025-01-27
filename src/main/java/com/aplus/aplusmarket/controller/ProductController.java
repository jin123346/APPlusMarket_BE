package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.product.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
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
    public List<Product> SelectAllProducts() {
        List<Product> product = productService.SelectAllProducts();
        log.info("products : " + product);
        return product;
    }
    //선택한 상품 정보 가지고 오기
    @GetMapping("/{id}")
    public ProductDTO SelectProductById(@PathVariable String id) {
        ProductDTO productDTO = productService.SelectProductById(id);
        log.info("products : " + productDTO);
        return productDTO;
    }
    //상품 등록
    @PostMapping(value = "/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public boolean insertProduct(
            @ModelAttribute ProductDTO productDTO,
            @RequestPart("images") List<MultipartFile> images) throws IOException {
        log.info("productDTO.getProduct_Images() : " + productDTO.getProductImages());
        boolean check = productService.InsertProduct(productDTO,images);
        return check;
    }
    //상품 삭제
    @DeleteMapping("/delete/{id}")
    public boolean DeleteProductById(@PathVariable String id) {
        boolean check = productService.DeleteProductById(id);
        return check;
    }
    //상품 페이징 처리
    @GetMapping("/listpage")
    public List<Product> getProducts(
            @RequestParam (defaultValue = "1") int page,
            @RequestParam (defaultValue = "10") int pageSize
    ){
        
        return productService.SelectProductsByPage(page,pageSize);
    }
    @PutMapping("/update")
    public boolean updateProduct(@RequestBody ProductDTO productDTO){
        boolean check = productService.UpdateProduct(productDTO);
        return check;
    }
}
