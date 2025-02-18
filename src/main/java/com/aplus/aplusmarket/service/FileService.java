package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.entity.Product_Images;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileService {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    private final String USER_DIR = System.getProperty("user.dir");



    //profile 파일 업로드

    public String getUploadPathForProfile(MultipartFile file) throws IOException {

        String profilePath = USER_DIR+"/"+uploadPath+"/profile/";
        log.info("경로/!!! "+profilePath);
        File uploadFolder = new File(profilePath);
        if(!uploadFolder.exists()){
            boolean created = uploadFolder.mkdirs();
            if(created){
                log.info("폴더 생성완료");
            }else{
                log.error("폴더 생성 안됨");
                return "";
            }

        }
        String originalFileName = file.getOriginalFilename();
        if(originalFileName ==null){
            log.info("파일이름이 없다.");
            return "";
        }
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID()+extension;
        String filePath = profilePath+savedFileName;

        File destFile = new File(filePath);
        file.transferTo(destFile);
        log.info("이미지 업로드 성공: " + filePath);
        return savedFileName;


    }
    /**
     * 파일 삭제 메서드
     */
    public boolean deleteFile(String uuidName,String path) {
        try {
            File file = new File(path + "/" + uuidName);
            if (file.exists()) {
                return file.delete(); // ❌ 실제 파일 삭제
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 중 오류 발생: " + uuidName, e);
        }
    }

    public Product_Images uploadProductImage(MultipartFile file,String path,long productId) throws IOException {

        String profilePath = path;
        log.info("경로/!!! "+profilePath);
        File uploadFolder = new File(profilePath);
        if(!uploadFolder.exists()){
            boolean created = uploadFolder.mkdirs();
            if(created){
                log.info("폴더 생성완료");
            }else{
                log.error("폴더 생성 안됨");
                return null;
            }

        }
        String originalFileName = file.getOriginalFilename();
        if(originalFileName ==null){
            log.info("파일이름이 없다.");
            return null;
        }
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = UUID.randomUUID()+extension;
        String filePath = profilePath+savedFileName;

        File destFile = new File(filePath);
        file.transferTo(destFile);
        log.info("이미지 업로드 성공: " + filePath);


        Product_Images productImages = new Product_Images();
        productImages.setProductId(productId);
        productImages.setOriginalName(originalFileName);
        productImages.setUuidName(savedFileName);


        return productImages;

    }
}
