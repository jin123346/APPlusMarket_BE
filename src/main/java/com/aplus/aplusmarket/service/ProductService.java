package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.document.Products;
import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.product.FindProduct;
import com.aplus.aplusmarket.dto.product.requests.ImageItemDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductListRequestDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductModifyRequestDTO;
import com.aplus.aplusmarket.dto.product.requests.ProductRequestDTO;
import com.aplus.aplusmarket.dto.product.response.ProductDTO;
import com.aplus.aplusmarket.dto.product.Product_ImagesDTO;
import com.aplus.aplusmarket.dto.product.response.ProductResponseCardDTO;
import com.aplus.aplusmarket.entity.Product;
import com.aplus.aplusmarket.entity.ProductResponseCard;
import com.aplus.aplusmarket.entity.Product_Images;
import com.aplus.aplusmarket.mapper.product.ProductImageMapper;
import com.aplus.aplusmarket.mapper.product.ProductMapper;
import com.aplus.aplusmarket.repository.ProductsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
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
    private final ProductsRepository productsRepository;
    private final FileService fileService;
    private final ObjectMapper objectMapper;
    //파일 업로드 경로
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    private final String USER_DIR = System.getProperty("user.dir");

    //상품 이미지 등록
    @Transactional
    public ResponseDTO insertProduct(ProductRequestDTO productRequestDTO, List<MultipartFile> images){
        try{
            Product product = toEntity(productRequestDTO);
            int index =0;
            //엔티티로 변경한 product 의 정보를 먼저 등록 합니다.
            boolean result = productMapper.InsertProduct(product);
            // 이미지 파일을 등록할 경로를 지정 합니다. 이때 InsertProduct로 전달 받은 product.getId()를 사용합니다
            // 경로는 uploads/상품번호/ 이렇게 저장 됩니다.
            File productFolder = new File(USER_DIR+"/"+uploadPath+"/"+product.getId().toString());
            //경로가 없을경우 폴더를 생성 하는데 기본적으로는 그냥 생성 한다고 보면 됩니다.
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

                    // 상품 이미지에 포함 해야 하는 정보들을 저장 합니다.
                    Product_Images productImages = new Product_Images();
                    productImages.setProductId(product.getId());
                    productImages.setOriginalName(originalName);
                    productImages.setUuidName(uuidName);
                    productImages.setSequence(index);
                    productImageMapper.InsertProductImage(productImages);
                    //index 값을 증가 시켜 몆번째 사진인지를 구분 지어 줍니다.
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

    // 상품 세부 정보 가지고 오는 기능
    public ResponseDTO selectProductById(String id) {
        try{
            //전달 받은 상품 번호를 이용하여 상품의 세부 정보를 가지고 옵니다.
            Product product = productMapper.SelectProductById(Long.parseLong(id));
            log.info("product : "+product);
            ProductDTO productDTO = toDTO(product);

            if(product == null){
                return ErrorResponseDTO.of(2004,"상품을 찾을 수 없습니다.");
            }
            log.info("🔥 [DEBUG] 조회된 상품 ID: {}", product.getId());


            // 상품 번호를 입력받아 등록되어 있는 상품 이미지를 리스트 형식으로 가지고 옵니다
            List<Product_Images> productImages = productImageMapper.SelectProductImageByProductId(product.getId());
            // Product_Images -> Product_ImagesDTO 변환
            log.info(" 몇개의 productImage?? : {}",productImages);

            if (productImages == null) {
                log.warn("⚠️ [WARNING] 상품 이미지가 없습니다. 빈 리스트로 대체합니다.");
                productImages = new ArrayList<>();
            }
            log.info(" 몇개의 productImage?? : {}",productImages);
                 List<Product_ImagesDTO> imageDTOs = new ArrayList<>();
                for(Product_Images image : productImages){
                 Product_ImagesDTO dto= new Product_ImagesDTO(
                                    image.getId(),
                                    image.getProductId(),
                                    image.getOriginalName(),
                                    image.getUuidName(),
                                    image.getSequence()
                            );
                 imageDTOs.add(dto);
                }

            // DTO에 이미지 리스트 추가
            productDTO.setImages(imageDTOs);

            log.info("Selected Product with Images: " + productDTO);
            return DataResponseDTO.of(productDTO, 2002, "상품 상세 정보 조회 성공");
        }catch (Exception e){
            log.error(e);
            return ErrorResponseDTO.of(2003, "상품 상세 정보 조회 실패 :"+e.getMessage());
        }
    }

    // 전체 상품 가지고 오기 (사용하지 않습니다)
    public List<Product> selectAllProducts() {
        List<Product> products = productMapper.SelectAllProducts(); // 전체 조회
        log.info("All Products: " + products);
        return products;
    }



    // 상품 삭제 기능(현재 상품에 대한 데이터를 삭제하는 식으로 되어 있습니다. 해당 기능은 조금 수정 해야 할것으로 보입니다.)
    public boolean deleteProductById(String id) {
        //현재는 상품 먼저 삭제하고 있는데 이미지 부터 먼저 삭제하는것부터 해야 합니다.
        Product product = productMapper.SelectProductById(Long.parseLong(id)); // 존재 여부 확인
        if (product == null) {
            log.warn("No product found to delete with ID: " + id);
            return false;
        }
        boolean result = productMapper.DeleteProduct(Long.parseLong(id)); // 삭제 실행
        return result;
    }

    //상품 페이징 처리 기능 (메인 화면)
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


    //나의 현재 판매중인 상품 조회
    public ResponseDTO selectProductByIdForSelling(ProductListRequestDTO productListRequestDTO){
        try{
            List<ProductResponseCard> products
                    = productMapper.selectProductByIdForStatus(productListRequestDTO.getLastIndex(),productListRequestDTO.getUserId(),productListRequestDTO.getStatus());
            log.info(products);
            if(products == null || products.isEmpty()){
                return ResponseDTO.of("success", 2006,"판매중인 상품이 없습니다.");
            }



            List<ProductResponseCardDTO> productList = products.stream().map(this::toDTO).toList();
            return DataResponseDTO.of(productList,2007,"판매중인 상품 조회 성공");

        }catch (Exception e){
            log.error("상품 목록 조회 실패", e);
            return ErrorResponseDTO.of(2005, "상품 목록 조회에 실패 했습니다.");
        }
    }

    //나의 구매완료된 상품 조회
    public ResponseDTO selectProductByIdForCompleted(ProductListRequestDTO productListRequestDTO){
        try{
            List<ProductResponseCard> products
                    = productMapper.selectProductByIdForCompleted(productListRequestDTO.getLastIndex(),productListRequestDTO.getUserId(),"Sold");
            log.info(products);
            if(products == null || products.isEmpty()){
                return ResponseDTO.of("success", 2006,"구매완료 및 판매완료 상품이 없습니다.");
            }

            List<ProductResponseCardDTO> productList = products.stream().map(this::toDTO).toList();
            return DataResponseDTO.of(productList,2007,"구매완료 및 판매완료 상품 조회 성공");

        }catch (Exception e){
            log.error("상품 목록 조회 실패", e);
            return ErrorResponseDTO.of(2005, "상품 목록 조회에 실패 했습니다.");
        }
    }

    //끌어올리기,
    public ResponseDTO reloadProduct(Long productId){
        try {
            int result = productMapper.updateReload(productId);

            if(result != 1 ){
                return ErrorResponseDTO.of(2010,"끌어올리기 실패");
            }
            return ResponseDTO.of("Success",2008,"끌어올리기 성공");

        }catch (Exception e){
            log.error(e.getMessage());
           return ErrorResponseDTO.of(2010,"끌어올리기 실패");

        }
    }

    // => reload 업데이트 ,

    //후기보기

    // 후기 작성하기

    //숨김처리 // 숨김해제
    public ResponseDTO updateStatus(Long productId,String status){
        try {
            int result = productMapper.updateStatus(productId,status);

            if(result != 1 ){
                return ErrorResponseDTO.of(2010,status +"실패");
            }
            return ResponseDTO.of("Success",2008,"끌어올리기 성공");

        }catch (Exception e){
            log.error(e.getMessage());
            return ErrorResponseDTO.of(2010,status +"실패");

        }
    }


    public ResponseDTO selectProductForModify(Long productId,Long userId){
        try {

            Product product = productMapper.SelectProductByIdForModify(productId);

            log.info("수정할 product : {}",product);
            if(product.getSellerId() != userId){
                return ErrorResponseDTO.of(2020,"권한이 없습니다.");
            }

            ProductDTO productDTO = toDTO(product);
            if(product.getFindProductId() != null){
               Optional<Products> opt = productsRepository.findById(product.getFindProductId());
               if(opt.isPresent()){
                   FindProduct findProduct  = FindProduct.toDTO(opt.get());
                   productDTO.setFindProduct(findProduct);
               }
            }
          List<Product_Images> images =productImageMapper.SelectProductImageByProductId(product.getId());
            List<Product_ImagesDTO> imageDTOs = new ArrayList<>();
            for(Product_Images image : images){
                Product_ImagesDTO dto= new Product_ImagesDTO(
                        image.getId(),
                        image.getProductId(),
                        image.getOriginalName(),
                        image.getUuidName(),
                        image.getSequence()
                );
                imageDTOs.add(dto);
            }
            // DTO에 이미지 리스트 추가
            productDTO.setImages(imageDTOs);

            return DataResponseDTO.of(productDTO,2022,"성공");

        }catch (Exception e){
            log.error(e.getMessage());
            return ErrorResponseDTO.of(2020,"업데이트 실패");

        }
    }


    //상품 업데이트 (수정)

    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO updateProduct(ProductModifyRequestDTO requestDTO){
        try{
            String path= USER_DIR+"/"+uploadPath+"/"+requestDTO.getId().toString();

            //삭제 먼저,
            if (requestDTO.getRemovedImages() != null) {

                for(long id : requestDTO.getRemovedImages()){
                    Product_Images deletedImage =productImageMapper.SelectProductImageById(id);
                    fileService.deleteFile(deletedImage.getUuidName(),path);
                     productImageMapper.deleteById(id);
                }

            }
            //기존 이미지 순서 업데이트,

            int length = 0;
            if (requestDTO.getExistingImages() != null) {
                for (ImageItemDTO img : requestDTO.getExistingImages()) {
                    productImageMapper.updateSequence(Long.valueOf(img.getId()), length);
                    length ++;
                }
            }

            //새로운 이미지 업로드

            // 새로운 이미지 업로드 및 저장
            if (requestDTO.getNewImages() != null) {
                for (MultipartFile file : requestDTO.getNewImages()) {
                    Product_Images productImages = fileService.uploadProductImage(file,path,requestDTO.getId());
                    if(productImages != null){
                        //에러처리
                        throw new RuntimeException(" productImages가 null, 업로드 실패!");

                    }
                    productImages.setSequence(length);
                    productImageMapper.InsertProductImage(productImages);
                    length++;
                }
            }
            //DB 데이터 갱신


            //제품 정보 업데이트
            Product product = Product.builder()
                    .id(requestDTO.getId())
                    .findProductId(requestDTO.getFindProduct())
                    .brand(requestDTO.getBrand())
                    .category(requestDTO.getCategory())
                    .productName(requestDTO.getProductName())
                    .content(requestDTO.getContent())
                    .title(requestDTO.getTitle())
                    .isNegotiable(requestDTO.getIsNegotiable())
                    .updatedAt(LocalDateTime.now())
                    .isPossibleMeetYou(requestDTO.getIsPossibleMeetYou())
                    .price(requestDTO.getPrice())
                    .registerIp(requestDTO.getRegisterIp())
                    .build();


            boolean result = productMapper.updateProduct(product);

            if (!result) {
                throw new RuntimeException(" 제품 업데이트 실패!");
            }
            return ResponseDTO.of("success", 2036,"제품 수정완료");


        }catch (Exception e){
            return ErrorResponseDTO.of(2030,"에러 발생");
        }

    }



    public void fileUpload(List<MultipartFile> images , String dirPath, long id,int index){
        try{
            File productFolder = new File(dirPath);
            //경로가 없을경우 폴더를 생성 하는데 기본적으로는 그냥 생성 한다고 보면 됩니다.
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

                    // 상품 이미지에 포함 해야 하는 정보들을 저장 합니다.
                    Product_Images productImages = new Product_Images();
                    productImages.setProductId(id);
                    productImages.setOriginalName(originalName);
                    productImages.setUuidName(uuidName);
                    productImages.setSequence(index);
                    productImageMapper.InsertProductImage(productImages);
                    //index 값을 증가 시켜 몆번째 사진인지를 구분 지어 줍니다.
                    index++;
                }
            }else {
                throw new Exception("Image가 들어오지 않음!");
            }
        }catch (Exception e){

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
                .brand(productRequestDTO.getBrand())
                .findProductId(productRequestDTO.getFindProductId())
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
                .brand(product.getBrand())
                .buyerId(product.getBuyerId())
                .ProductImages(product.getImages().stream().map(Product_ImagesDTO::toDTO).toList())
                .build();
    }

    //상품 리스트 화면 출력 에서 데이터 변경
    private ProductResponseCardDTO toDTO(ProductResponseCard entity) {
        return ProductResponseCardDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .productName(entity.getProductName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .reloadAt(entity.getReloadAt())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .sellerId(entity.getSellerId())
                .isNegotiable(entity.getIsNegotiable())
                .isPossibleMeetYou(entity.getIsPossibleMeetYou())
                .category(entity.getCategory())
                .productImage(entity.getProductImage()) // 이미지 필드 추가
                .registerLocation(entity.getRegisterLocation())
                .uuidName(entity.getUuidName())
                .buyerId(entity.getBuyerId())
                .build();
    }
}
