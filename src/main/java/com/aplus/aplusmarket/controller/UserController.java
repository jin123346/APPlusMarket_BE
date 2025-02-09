package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.requset.FindUserRequestDTO;
import com.aplus.aplusmarket.dto.auth.response.AddressBookResponseDTO;
import com.aplus.aplusmarket.dto.product.request;
import com.aplus.aplusmarket.service.AddressService;
import com.aplus.aplusmarket.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserController {


    private final UserService userService;
    private final AddressService addressService;

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

    //주소지 홈 검색
    @GetMapping("/my/address/{id}")
    public ResponseEntity findAddress(@PathVariable(value = "id") Long id, HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        log.info("id : " + id);
        if(id != userId){return ResponseEntity.status(401).body(ErrorResponseDTO.of(1325,"허가되지 않은 유저입니다."));}

        ResponseDTO responseDTO = addressService.selectAddressByUserId(userId);

        if(responseDTO instanceof DataResponseDTO){
            return ResponseEntity.ok().body((DataResponseDTO)responseDTO);
        }
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/my/address/register")
    public ResponseEntity registerAddress(@RequestBody AddressBookResponseDTO address,HttpServletRequest request){

       ResponseDTO responseDTO =  addressService.insertAddress(address);

        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/my/address/modify")
    public ResponseEntity modify(@RequestBody AddressBookResponseDTO address,HttpServletRequest request){
        Long userId = (Long) request.getAttribute("id");
        log.info("수정 시작  id : " + address.getUserId());
        if(address.getUserId() != userId){return ResponseEntity.status(401).body(ErrorResponseDTO.of(1325,"허가되지 않은 유저입니다."));}

        ResponseDTO responseDTO =  addressService.updateAddress(address);

        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/my/address/{sessionUserId}/deleted/{id}")
    public ResponseEntity delete(@PathVariable(value = "sessionUserId") Long sessionUserId,@PathVariable(value = "id") Long addressId){

        ResponseDTO responseDTO =  addressService.deleteAddress(addressId,sessionUserId);
        return ResponseEntity.ok().body(responseDTO);
    }
}
