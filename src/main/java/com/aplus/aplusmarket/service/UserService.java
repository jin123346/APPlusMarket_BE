package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.requset.FindUserRequestDTO;
import com.aplus.aplusmarket.dto.auth.requset.ProfileUpdateRequestDTO;
import com.aplus.aplusmarket.dto.auth.response.MyInfoUser;
import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileService fileService;
    private final EmailService emailService;

    private final StringRedisTemplate redisTemplate;
    private static final int CODE_EXPIRY_MINUTES = 3;

    public ResponseDTO selectUserByIdForMyInfo(Long id){

        try{
            Optional<User> opt  =  userMapper.selectUserById(id);
            if(opt.isEmpty()){

                return ResponseDTO.error(ResponseCode.MEMBER_NOT_FOUND);
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
            return ResponseDTO.success(ResponseCode.MEMBER_INFO_SUCCESS,myInfoUser);


        }catch (Exception e){
            log.info("회원 조회시 에러 발생 {}",e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_INTERNAL_SERVER_ERROR);
        }

    }


    public ResponseDTO selectUserByUidForMyInfo(String uid){

        try{
            Optional<User> opt  =  userMapper.selectUserByUid(uid);
            if(opt.isEmpty()){
                return ResponseDTO.error(ResponseCode.MEMBER_NOT_FOUND);

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
                    .profileImg(user.getProfileImg())
                    .build();
            log.info("user 정보 : {}",myInfoUser);
            return ResponseDTO.success(ResponseCode.MEMBER_INFO_SUCCESS,myInfoUser);


        }catch (Exception e){
            log.info("회원 조회시 에러 발생 {}",e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_INTERNAL_SERVER_ERROR);
        }

    }


    // 회원정보 수정처리

    public ResponseDTO updateUserByIdForMyInfo(MyInfoUser myInfoUser){

        try {
            log.info("MyInfo {}",myInfoUser);
            if(myInfoUser.getId() <= 0){
                return ResponseDTO.error(ResponseCode.MEMBER_FIND_ID_NOT_FOUND);
            }
           int result =  userMapper.updateUserById(myInfoUser.getId(),myInfoUser.getEmail(), myInfoUser.getHp(), myInfoUser.getBirthDay(),myInfoUser.getNickName());
           if(result != 1){
               return ResponseDTO.error(ResponseCode.MEMBER_UPDATE_FAIL);
           }
           log.info(" 유저 업데이트 성공 : {}",result);
            return ResponseDTO.success(ResponseCode.MEMBER_UPDATE_SUCCESS);


        }catch (Exception e){
            log.info("회원 수정시 오류 발생 {}",e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_UPDATE_FAIL);
        }

    }

    public ResponseDTO findUidByNameAndEmail(FindUserRequestDTO requestDTO){

        try{
            if(requestDTO.getEmail()==null || requestDTO.getName()==null){
                return ResponseDTO.error(ResponseCode.MEMBER_NOT_FOUND);

            }
            String uid = userMapper.selectUserByNameAndEmail(requestDTO.getName(),requestDTO.getEmail());
            log.info("조회 결과 : {}",uid);
            if( uid == null){
                return ResponseDTO.success(ResponseCode.MEMBER_FIND_ID_NOT_FOUND);
            }
             requestDTO.setUid(uid);
            return ResponseDTO.success(ResponseCode.MEMBER_FIND_ID_SUCCESS,requestDTO);


        }catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_INTERNAL_SERVER_ERROR);

        }

    }

    public ResponseDTO findUidByUidAndEmail(FindUserRequestDTO requestDTO){

        try{
            if(requestDTO.getEmail()==null || requestDTO.getUid()==null){
                return ResponseDTO.error(ResponseCode.MEMBER_NOT_FOUND);

            }
            Long id = userMapper.selectUserByUidAndEmail(requestDTO.getUid(),requestDTO.getEmail());
            log.info("조회 결과 : {}",id);
            if( id == null){
                return ResponseDTO.success(ResponseCode.MEMBER_FIND_ID_NOT_FOUND);

            }
            requestDTO.setId(id);
            return ResponseDTO.success(ResponseCode.MEMBER_FIND_ID_SUCCESS,requestDTO);


        }catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_INTERNAL_SERVER_ERROR.getHttpStatus(),ResponseCode.MEMBER_INTERNAL_SERVER_ERROR,"아이디 찾기 중 조회 오류");

        }

    }

    public ResponseDTO updatePassword(FindUserRequestDTO requestDTO){

        try{
            if(requestDTO.getId()==0 || requestDTO.getPassword()==null){
                return ResponseDTO.error(ResponseCode.MEMBER_NOT_FOUND);
            }
            Optional<User> opt =  userMapper.selectUserById(requestDTO.getId());
            if(opt.isEmpty()){
                return ResponseDTO.success(ResponseCode.MEMBER_FIND_ID_NOT_FOUND);
            }
                String newPassword = passwordEncoder.encode(requestDTO.getPassword());

             int result = userMapper.updatePassById(requestDTO.getId(),newPassword);
            if(result != 1){
                return ResponseDTO.error(ResponseCode.MEMBER_PASS_UPDATE_FAIL);

            }
            return ResponseDTO.success(ResponseCode.MEMBER_PASS_UPDATE_SUCCESS);


        }catch (Exception e){
            log.error(e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_INTERNAL_SERVER_ERROR.getHttpStatus(),ResponseCode.MEMBER_INTERNAL_SERVER_ERROR,"비밀번호 업데이트 중  오류");

        }

    }




    public ResponseDTO profileUpdate(MultipartFile images,int id)  {
        try{
            Optional<User> opt = userMapper.selectUserById(id);
            if(opt.isEmpty()){
                return ResponseDTO.error(ResponseCode.MEMBER_NOT_FOUND);

            }
            User user = opt.get();

            String savedFileName = fileService.getUploadPathForProfile(images);

            if (savedFileName == "") {
                return ResponseDTO.error(ResponseCode.PROFILE_UPDATE_FAIL);
            }

            int result = userMapper.updateProfileImage(user.getId(),savedFileName);

            if(result != 1){
                return ResponseDTO.error(ResponseCode.PROFILE_UPDATE_FAIL);

            }
            return ResponseDTO.success(ResponseCode.PROFILE_UPDATE_SUCCESS);



        }catch (Exception e){
            log.error("이미지 업데이트중 에러 발생 : {}",e.getMessage());
            throw new CustomException(ResponseCode.MEMBER_INTERNAL_SERVER_ERROR.getHttpStatus(),ResponseCode.MEMBER_INTERNAL_SERVER_ERROR,"이미지 업데이트 도중 문제 발생");

        }

    }





    //이메일 인증 요청
    public ResponseDTO verificationEmail(String email){

        try{

            //현재 이메일이 존재하는지 확인하기
            Optional<User> user = userMapper.selectUserByEmail(email);
            if(user.isPresent()){
                return ResponseDTO.error(ResponseCode.EMAIL_ALREADY_EiXST);
            }

             String verificationCode = generateVerificationCode();
            redisTemplate.opsForValue().set(email,verificationCode,CODE_EXPIRY_MINUTES, TimeUnit.MINUTES);
            emailService.sendVerificationEmail(email,verificationCode);

            return ResponseDTO.success(ResponseCode.EMAIL_SEND_SUCCESS);

        }catch (Exception e){
            log.error(e.getMessage());
            return ResponseDTO.error(ResponseCode.INTERNAL_SERVER_ERROR);

        }


    }

    public ResponseDTO verifyCode(String email, String inputCode){
        try{
            String storedCode = redisTemplate.opsForValue().get(email);
            //유효기간 만료
            if (storedCode == null) {
                return ResponseDTO.error(ResponseCode.EMAIL_CODE_TIMEOUT);
            }

            if (!storedCode.equals(inputCode)) {
                redisTemplate.delete(email);
                return ResponseDTO.error(ResponseCode.EMAIL_CODE_NOT_CORRECT);


            }

            redisTemplate.delete(email);
            return ResponseDTO.error(ResponseCode.EMAIL_CODE_CORRECT);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

    }

    private String generateVerificationCode() {
        SecureRandom secureRandom = new SecureRandom();
        int code = 10000000 + secureRandom.nextInt(90000000); // 10000000 ~ 99999999
        return String.valueOf(code);
    }







}
