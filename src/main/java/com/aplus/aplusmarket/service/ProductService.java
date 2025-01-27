package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.product.ProductDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.mapper.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    // Insert Product
    public boolean InsertProduct(ProductDTO productDTO) {
        Product product = toEntity(productDTO); // DTO -> Entity 변환
        boolean result = productMapper.InsertProduct(product); // Insert 실행
        log.info("Insert Product Result: " + result);
        return result;
    }

    // Select Product by ID
    public ProductDTO SelectProductById(String id) {
        Product product = productMapper.SelectProductById(Long.parseLong(id)); // Mapper 호출
        if (product == null) {
            log.warn("No product found with ID: " + id);
            return null;
        }
        log.info("Selected Product: " + product);
        return toDTO(product); // Entity -> DTO 변환
    }

    // Select All Products
    public List<Product> SelectAllProducts() {
        List<Product> products = productMapper.SelectAllProducts(); // 전체 조회
        log.info("All Products: " + products);
        return products;
    }

    // Update Product
    public ProductDTO UpdateProduct(ProductDTO productDTO) {
        Product product = toEntity(productDTO); // DTO -> Entity 변환
        boolean result = productMapper.UpdateProduct(product); // Update 실행
        if (result) {
            log.info("Product Updated: " + product);
            return toDTO(product); // 성공 시 DTO 반환
        }
        log.warn("Failed to update product: " + productDTO);
        return null;
    }

    // Delete Product by ID
    public boolean DeleteProductById(String id) {
        Product product = productMapper.SelectProductById(Long.parseLong(id)); // 존재 여부 확인
        if (product == null) {
            log.warn("No product found to delete with ID: " + id);
            return false;
        }
        boolean result = productMapper.DeleteProduct(Long.parseLong(id)); // 삭제 실행
        return result;
    }
    public List<Product> SelectProductsByPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize; // 페이지 번호를 기반으로 offset 계산
        List<Product> products = productMapper.SelectProductsPage(pageSize, offset);
        log.info("Products (Page: " + page + ", PageSize: " + pageSize + "): " + products);
        return products;
    }
    // DTO -> Entity 변환
    private Product toEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .title(productDTO.getTitle())
                .productImages(productDTO.getProductImages())
                .productName(productDTO.getProductName())
                .content(productDTO.getContent())
                .registerLocation(productDTO.getRegisterLocation())
                .registerIp(productDTO.getRegisterIp())
                .createdAt(productDTO.getCreatedAt())
                .updatedAt(productDTO.getUpdatedAt())
                .reloadAt(productDTO.getReloadAt())
                .price(productDTO.getPrice())
                .status(productDTO.getStatus())
                .deletedAt(productDTO.getDeletedAt())
                .sellerId(productDTO.getSellerId())
                .isNegotiable(productDTO.getIsNegotiable())
                .isPossibleMeetYou(productDTO.getIsPossibleMeetYou())
                .category(productDTO.getCategory())
                .build();
    }

    // Entity -> DTO 변환
    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .productImages(product.getProductImages())
                .productName(product.getProductName())
                .content(product.getContent())
                .registerLocation(product.getRegisterLocation())
                .registerIp(product.getRegisterIp())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .reloadAt(product.getReloadAt())
                .price(product.getPrice())
                .status(product.getStatus())
                .deletedAt(product.getDeletedAt())
                .sellerId(product.getSellerId())
                .isNegotiable(product.getIsNegotiable())
                .isPossibleMeetYou(product.getIsPossibleMeetYou())
                .category(product.getCategory())
                .build();
    }
}
