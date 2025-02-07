package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.requset.FindUserRequestDTO;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

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


    // 회원정보 수정처리

    public ResponseDTO updateUserByIdForMyInfo(MyInfoUser myInfoUser){

        try {
            log.info("MyInfo {}",myInfoUser);
            if(myInfoUser.getId() <= 0){
                return  ErrorResponseDTO.of(1203,"해당하는 id가 없습니다.");
            }
           int result =  userMapper.updateUserById(myInfoUser.getId(),myInfoUser.getEmail(), myInfoUser.getHp(), myInfoUser.getBirthDay(),myInfoUser.getNickName());
           if(result != 1){
               return  ErrorResponseDTO.of(1203,"업데이트가 이뤄지지않았습니다.");
           }
           log.info(" 유저 업데이트 성공 : {}",result);
            return ResponseDTO.of("success",1202,"회원정보 수정 성공");


        }catch (Exception e){
            log.info("회원 수정시 오류 발생 {}",e.getMessage());
            return  ErrorResponseDTO.of(1204,"회원수정시 오류 발생");
        }

    }

    public ResponseDTO findUidByNameAndEmail(FindUserRequestDTO requestDTO){

        try{
            if(requestDTO.getEmail()==null || requestDTO.getName()==null){
                return ErrorResponseDTO.of(1007,"조회할 값이 없습니다.");
            }
            String uid = userMapper.selectUserByNameAndEmail(requestDTO.getName(),requestDTO.getEmail());
            log.info("조회 결과 : {}",uid);
            if( uid == null){
                return ErrorResponseDTO.of(1012,"해당하는 아이디가 없습니다.");
            }
             requestDTO.setUid(uid);
            return DataResponseDTO.of(requestDTO,1006,"아이디찾기 성공");

        }catch (Exception e){
            log.error(e.getMessage());
            return ErrorResponseDTO.of(1007,"아이디 찾기 중 조회 오류");

        }

    }

    public ResponseDTO findUidByUidAndEmail(FindUserRequestDTO requestDTO){

        try{
            if(requestDTO.getEmail()==null || requestDTO.getUid()==null){
                return ErrorResponseDTO.of(1007,"조회할 값이 없습니다.");
            }
            Long id = userMapper.selectUserByUidAndEmail(requestDTO.getUid(),requestDTO.getEmail());
            log.info("조회 결과 : {}",id);
            if( id == null){
                return ErrorResponseDTO.of(1012,"해당하는 아이디가 없습니다.");
            }
            requestDTO.setId(id);
            return DataResponseDTO.of(requestDTO,1006,"아이디찾기 성공");

        }catch (Exception e){
            log.error(e.getMessage());
            return ErrorResponseDTO.of(1007,"아이디 찾기 중 조회 오류");

        }

    }

    public ResponseDTO updatePassword(FindUserRequestDTO requestDTO){

        try{
            if(requestDTO.getId()==0 || requestDTO.getPassword()==null){
                return ErrorResponseDTO.of(1007,"조회할 값이 없습니다.");
            }
            Optional<User> opt =  userMapper.selectUserById(requestDTO.getId());
            if(opt.isEmpty()){
                return ErrorResponseDTO.of(1012,"해당하는 user 가 없습니다.");
            }
                String newPassword = passwordEncoder.encode(requestDTO.getPassword());

             int result = userMapper.updatePassById(requestDTO.getId(),newPassword);
            if(result != 1){
                return ErrorResponseDTO.of(1007,"비밀번호 업데이트에 실패했습니다.");
            }
            return ResponseDTO.of("success",1110,"비밀번호 변경 성공");

        }catch (Exception e){
            log.error(e.getMessage());
            return ErrorResponseDTO.of(1007,"비밀번호 업데이트 중  오류");

        }

    }






}
