package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.product.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/inssert")
    public boolean InsertProduct(@RequestBody ProductDTO productDTO){
        log.info("productDTO.getProduct_Images() : " + productDTO.getProductImages());
        boolean check = productService.InsertProduct(productDTO);
        return check;
    }
    //상품 삭제
    @DeleteMapping("/deleteproduct/{id}")
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

}
