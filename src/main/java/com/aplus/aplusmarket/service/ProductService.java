package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductRequestDTO;
import com.aplus.aplusmarket.dto.product.response.ProductDTO;
import com.aplus.aplusmarket.dto.product.Product_ImagesDTO;
import com.aplus.aplusmarket.dto.product.response.ProductResponseCardDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import com.aplus.aplusmarket.entity.Product_Images;
import com.aplus.aplusmarket.mapper.product.ProductImageMapper;
import com.aplus.aplusmarket.mapper.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
/*
* 2024.02.04 이도영 상품 페이징 처리 기능 수정
* */
@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    //파일 업로드 경로
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    private final String USER_DIR = System.getProperty("user.dir");
    // Insert Product

    @Transactional
    public ResponseDTO insertProduct(ProductRequestDTO productRequestDTO, List<MultipartFile> images){
        try{
            Product product = toEntity(productRequestDTO);
            int index =0;
            boolean result = productMapper.InsertProduct(product);


            File productFolder = new File(USER_DIR+"/"+uploadPath+"/"+product.getId().toString());

            if (!productFolder.exists()) {
                productFolder.mkdirs();
            }
            if (images != null) {
                for (MultipartFile file : images) {
                    // 원본 파일명에서 확장자 추출
                    String originalName = file.getOriginalFilename();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }
                    // UUID 파일명 생성
                    String uuidName = UUID.randomUUID().toString() + extension;
                    // 최종 저장 경로
                    File dest = new File(productFolder,uuidName);
                    // 파일 저장 (disk write)
                    file.transferTo(dest);

                    Product_Images productImages = new Product_Images();
                    productImages.setProductId(product.getId());
                    productImages.setOriginalName(originalName);
                    productImages.setUuidName(uuidName);
                    productImages.setImageIndex(index);
                    productImageMapper.InsertProductImage(productImages);
                    index++;
                }
            }else {
                throw new Exception("Image가 들어오지 않음!");
            }
            return ResponseDTO.of("success",2000,"상품 등록 성공");
        }catch (Exception e){
            log.error(e);
            return ErrorResponseDTO.of(2001, "상품 등록 실패 :"+e.getMessage());
        }
    }

    // Select Product by ID
    public ResponseDTO selectProductById(String id) {
        try{
            Product product = productMapper.SelectProductById(Long.parseLong(id));
            log.info("product : "+product);
            ProductDTO productDTO = toDTO(product);
            List<Product_Images> productImages = productImageMapper.SelectProductImageByProductId(product.getId());
            // Product_Images -> Product_ImagesDTO 변환
            List<Product_ImagesDTO> imageDTOs = productImages.stream()
                    .map(image -> new Product_ImagesDTO(
                            image.getId(),
                            image.getProductId(),
                            image.getOriginalName(),
                            image.getUuidName(),
                            image.getImageIndex()
                    ))
                    .collect(Collectors.toList());

            // DTO에 이미지 리스트 추가
            productDTO.setImages(imageDTOs);

            log.info("Selected Product with Images: " + productDTO);
            return DataResponseDTO.of(productDTO, 2002, "상품 상세 정보 조회 성공");
        }catch (Exception e){
            log.error(e);
            return ErrorResponseDTO.of(2003, "상품 상세 정보 조회 실패 :"+e.getMessage());
        }
    }

    // Select All Products
    public List<Product> selectAllProducts() {
        List<Product> products = productMapper.SelectAllProducts(); // 전체 조회
        log.info("All Products: " + products);
        return products;
    }

    // Update Product
    public boolean updateProduct(ProductRequestDTO productDTO) {
        Product product = toEntity(productDTO); // DTO -> Entity 변환
        boolean result = productMapper.UpdateProduct(product); // Update 실행
        return result; // 성공 시 DTO 반환
    }

    // Delete Product by ID
    public boolean deleteProductById(String id) {
        Product product = productMapper.SelectProductById(Long.parseLong(id)); // 존재 여부 확인
        if (product == null) {
            log.warn("No product found to delete with ID: " + id);
            return false;
        }
        boolean result = productMapper.DeleteProduct(Long.parseLong(id)); // 삭제 실행
        return result;
    }
    public ResponseDTO selectProductsByPage(int page, int pageSize) {
        try {
            int offset = (page - 1) * pageSize;
            List<ProductResponseCard> dtoList = productMapper.SelectProductsPage(pageSize, offset);

            List<ProductResponseCardDTO> products = dtoList.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            log.info("Products (Page: " + page + ", PageSize: " + pageSize + "): " + products);
            return DataResponseDTO.of(products, 2004, "상품 목록 조회 성공");
        } catch (Exception e) {
            log.error("상품 목록 조회 실패", e);
            return ErrorResponseDTO.of(2005, "상품 목록 조회에 실패 했습니다.");
        }
    }
    // DTO -> Entity 변환
    private Product toEntity(ProductRequestDTO productRequestDTO) {
        return Product.builder()
                .id(productRequestDTO.getId())
                .title(productRequestDTO.getTitle())
                .productName(productRequestDTO.getProductName())
                .content(productRequestDTO.getContent())
                .registerLocation(productRequestDTO.getRegisterLocation())
                .registerIp(productRequestDTO.getRegisterIp())
                .createdAt(productRequestDTO.getCreatedAt())
                .updatedAt(productRequestDTO.getUpdatedAt())
                .reloadAt(productRequestDTO.getReloadAt())
                .price(productRequestDTO.getPrice())
                .status(productRequestDTO.getStatus())
                .deletedAt(productRequestDTO.getDeletedAt())
                .sellerId(productRequestDTO.getSellerId())
                .isNegotiable(productRequestDTO.getIsNegotiable())
                .isPossibleMeetYou(productRequestDTO.getIsPossibleMeetYou())
                .category(productRequestDTO.getCategory())
                .build();
    }

    // Entity -> DTO 변환
    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
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
                .nickName(product.getNickName())
                .isNegotiable(product.getIsNegotiable())
                .isPossibleMeetYou(product.getIsPossibleMeetYou())
                .category(product.getCategory())
                .build();
    }
    private ProductResponseCardDTO toDTO(ProductResponseCard dto) {
        return ProductResponseCardDTO.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .productName(dto.getProductName())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .sellerId(dto.getSellerId())
                .isNegotiable(dto.getIsNegotiable())
                .isPossibleMeetYou(dto.getIsPossibleMeetYou())
                .category(dto.getCategory())
                .productImage(dto.getProductImage()) // 이미지 필드 추가
                .build();
    }
}
