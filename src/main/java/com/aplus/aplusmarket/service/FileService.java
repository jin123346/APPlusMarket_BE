package com.aplus.aplusmarket.service;

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
}
