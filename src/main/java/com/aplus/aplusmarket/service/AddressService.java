package com.aplus.aplusmarket.service;


import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.response.AddressBookResponseDTO;
import com.aplus.aplusmarket.entity.AddressBook;
import com.aplus.aplusmarket.handler.CustomException;
import com.aplus.aplusmarket.handler.ResponseCode;
import com.aplus.aplusmarket.mapper.auth.AddressBookMapper;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
    2025.02.29 하진희 addressBook 서비스
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class AddressService {

    private final AddressBookMapper addressBookMapper;
    private final UserMapper userMapper;

    /**
     * 사용자 addressBook 찾기
     * @param userId
     * @return
     */
    public ResponseDTO selectAddressByUserId(Long userId){

        try {
            if(userId == 0 ){
              return ResponseDTO.error(ResponseCode.NO_INPUT_DATA);
            }

            List<AddressBook> addressBooks =  addressBookMapper.findByUserId(userId);
            log.info("조회된 주소 : {}",addressBooks);

            if(addressBooks.isEmpty() || addressBooks.size() == 0){
                return ResponseDTO.success(ResponseCode.ADDRESS_NOT_FOUND);
            }

           List<AddressBookResponseDTO> addressBookDtos = addressBooks.stream().map(
                   addressBook -> new AddressBookResponseDTO().toResponseDTO(addressBook))
                   .toList();
            log.info("조회된 주소 : {}",addressBookDtos);
            return ResponseDTO.success(ResponseCode.ADDRESS_FIND_SUCCESS,addressBookDtos);

                   // DataResponseDTO.of(addressBookDtos,1320,"주소 등록 확인");

        }
        catch(Exception e){
            log.error("주소 확인 중 에러 발생 "+e.getMessage());

            throw new CustomException(ResponseCode.ADDRESS_INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseDTO insertAddress(AddressBookResponseDTO addressBookResponseDTO){
        try{
            AddressBook addressBook = addressBookResponseDTO.toEntity();
            if(addressBook.isDefault()){
                addressBookMapper.updateAddressIsDefault(addressBook.getUserId());
            }

            long result = addressBookMapper.insertAddress(addressBook);
            if(result>0){
                Long generatedId = addressBook.getId();
                return ResponseDTO.success(ResponseCode.ADDRESS_CREATE_SUCCESS,generatedId);
            }

            return ResponseDTO.error(ResponseCode.ADDRESS_CREATE_FAILED);

        } catch (Exception e) {
            log.error("주소 등록 중 에러 발생 "+e.getMessage());
            throw new CustomException(ResponseCode.ADDRESS_INTERNAL_SERVER_ERROR);
        }

    }

    //주소 수정
    public ResponseDTO updateAddress(AddressBookResponseDTO addressBookResponseDTO){
        try{
            AddressBook addressBook = addressBookResponseDTO.toEntity();
            if(addressBook.isDefault() ){
                addressBookMapper.updateAddressIsDefault(addressBook.getUserId());
            }
            log.info(addressBookResponseDTO.toString());

            int result = addressBookMapper.updateAddressForModify(addressBook);
            if(result>0){
                return ResponseDTO.success(ResponseCode.ADDRESS_MODIFY_SUCCESS);
            }

            return ResponseDTO.error(ResponseCode.ADDRESS_MODIFY_FAILED);

        } catch (Exception e) {
            log.error("주소 수정 중 에러 발생 "+e.getMessage());
            throw new CustomException(ResponseCode.ADDRESS_INTERNAL_SERVER_ERROR);
        }
    }
    //주소 삭제
    @Transactional
    public ResponseDTO deleteAddress(Long addressId,Long userId){
        try{
            if(addressId == 0 ){
                return  ResponseDTO.success(ResponseCode.ADDRESS_DELETED_FAILED);
            }
            
          long id =  addressBookMapper.addressIsExist(addressId);
            if( userId != id){
                return ResponseDTO.error(ResponseCode.ADDRESS_DELETED_NOT_AUTHORITY);
            }

            int result = addressBookMapper.deleteAddressById(addressId);
            
            if(result> 0){
                return ResponseDTO.success(ResponseCode.ADDRESS_DELETED_SUCCESS);
            }

            return ResponseDTO.error(ResponseCode.ADDRESS_DELETED_FAILED);

        } catch (Exception e) {
            log.error("주소 등록 중 에러 발생 "+e.getMessage());
            throw new CustomException(ResponseCode.ADDRESS_INTERNAL_SERVER_ERROR);
        }
    }

}
