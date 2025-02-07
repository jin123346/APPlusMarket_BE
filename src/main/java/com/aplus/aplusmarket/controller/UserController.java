package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.requset.FindUserRequestDTO;
import com.aplus.aplusmarket.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {


    private final UserService userService;

    @PostMapping("/find/uid")
    public ResponseEntity findUid(@RequestBody FindUserRequestDTO user) {
        log.info("name : " + user.getName());
        log.info("email : " + user.getEmail());

        ResponseDTO responseDTO = userService.findUidByNameAndEmail(user);

        if(responseDTO instanceof DataResponseDTO){
            return ResponseEntity.ok().body((DataResponseDTO)responseDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/find/pass")
    public ResponseEntity findPass(@RequestBody FindUserRequestDTO user) {
        log.info("uid : " + user.getUid());
        log.info("email : " + user.getEmail());

        ResponseDTO responseDTO = userService.findUidByUidAndEmail(user);

        if(responseDTO instanceof DataResponseDTO){
            return ResponseEntity.ok().body((DataResponseDTO)responseDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/find/pass/change")
    public ResponseEntity findPassChange(@RequestBody FindUserRequestDTO user) {
        log.info("id : " + user.getId());
        ResponseDTO responseDTO = userService.updatePassword(user);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/my/profile/images/{id}")
    public ResponseEntity uploadProfileImages(@RequestPart("image") MultipartFile image,@PathVariable(value = "id")int id){
        log.info("여기로 들어옴 {}",image);

        if(image.isEmpty() || image==null){
            return ResponseEntity.ok().body(ErrorResponseDTO.of(1211,"파일이 없습니다."));
        }


        ResponseDTO responseDTO = userService.profileUpdate(image,id);
        if(responseDTO instanceof DataResponseDTO){
            return  ResponseEntity.ok().body((DataResponseDTO)responseDTO);
        }

        return ResponseEntity.ok().body(responseDTO);
    }
}
