package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductRequestDTO;
import com.aplus.aplusmarket.dto.product.response.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/insert")
    public ResponseEntity insertProduct(
            @ModelAttribute ProductRequestDTO productRequestDTO,
            @RequestPart("images") List<MultipartFile> images,
            HttpServletRequest request){
        Long id =(Long)request.getAttribute("id");
        log.info("id : "+ id);
        productRequestDTO.setSellerId(id);
        log.info("images : "+ images.size());
        ResponseDTO responseDTO = productService.insertProduct(productRequestDTO, images);
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
    public boolean updateProduct(@RequestBody ProductRequestDTO productRequestDTO){
        boolean check = productService.updateProduct(productRequestDTO);
        return check;
    }
}
