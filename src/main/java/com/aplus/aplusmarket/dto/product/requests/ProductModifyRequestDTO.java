package com.aplus.aplusmarket.dto.product.requests;

import com.aplus.aplusmarket.dto.product.Product_ImagesDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductModifyRequestDTO {
    private Long id;
    private String title;
    private String productName;
    private String content;
    private Integer price;
    private String status;
    private LocalDateTime deletedAt;
    private Long sellerId;
    private String nickName;
    private Boolean isNegotiable;
    private Boolean isPossibleMeetYou;
    private String category;
    private String brand;
    private String registerIp;
    private String findProduct;
    private Long buyerId;
    private List<Product_ImagesDTO> images;
    private List<ImageItemDTO> existingImages;

    private List<MultipartFile> newImages; // 새 이미지
    private List<Long> removedImages; // 삭제할 이미지 ID 리스트


}



