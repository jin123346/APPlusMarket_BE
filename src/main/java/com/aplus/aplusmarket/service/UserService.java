package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public ResponseDTO selectUserByIdForMyInfo(Long id){

        try{
            Optional<User> opt  =  userMapper.selectUserById(id);
            if(opt.isEmpty()){
                return ErrorResponseDTO.of(1201,"조회된 정보가 없습니다.");
            }

            User user=opt.get();
            MyInfoUser myInfoUser = MyInfoUser.builder()
                    .id(user.getId())
                    .uid(user.getUid())
                    .hp(user.getHp())
                    .name(user.getName())
                    .email(user.getEmail())
                    .nickName(user.getNickname())
                    .birthDay(user.getBirthday())
                    .build();
            log.info("user 정보 : {}",myInfoUser);

            return DataResponseDTO.of(myInfoUser,1200,"정보 조회 성공");

        }catch (Exception e){
            log.info("회원 조회시 에러 발생 {}",e.getMessage());
            return  ErrorResponseDTO.of(1201,"조회시 오류 발생");
        }

    }

    public ResponseDTO selectUserByUidForMyInfo(String uid){

        try{
            Optional<User> opt  =  userMapper.selectUserByUid(uid);
            if(opt.isEmpty()){
                return ErrorResponseDTO.of(1201,"조회된 정보가 없습니다.");
            }

            User user=opt.get();
            MyInfoUser myInfoUser = MyInfoUser.builder()
                    .id(user.getId())
                    .uid(user.getUid())
                    .hp(user.getHp())
                    .name(user.getName())
                    .nickName(user.getNickname())
                    .email(user.getEmail())
                    .birthDay(user.getBirthday())
                    .build();
            log.info("user 정보 : {}",myInfoUser);

            return DataResponseDTO.of(myInfoUser,1200,"정보 조회 성공");

        }catch (Exception e){
            log.info("회원 조회시 에러 발생 {}",e.getMessage());
            return  ErrorResponseDTO.of(1201,"조회시 오류 발생");
        }

    }


}
