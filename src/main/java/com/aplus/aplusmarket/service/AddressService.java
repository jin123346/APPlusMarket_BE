package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.auth.response.AddressBookResponseDTO;
import com.aplus.aplusmarket.entity.AddressBook;
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
              return   ErrorResponseDTO.of(1333,"해당하는 유저가 없습니다.");
            }

            List<AddressBook> addressBooks =  addressBookMapper.findByUserId(userId);
            if(addressBooks.isEmpty()){
                return ResponseDTO.of("success",1321,"등록된 주소가 없습니다.");
            }

           List<AddressBookResponseDTO> addressBookDtos = addressBooks.stream().map(
                   addressBook -> new AddressBookResponseDTO().toResponseDTO(addressBook))
                   .toList();
            log.info("조회된 주소 : {}",addressBookDtos);
            return DataResponseDTO.of(addressBookDtos,1320,"주소 등록 확인");

        }
        catch(Exception e){
            log.error("주소 확인 중 에러 발생 "+e.getMessage());
            return  ErrorResponseDTO.of(1333,"주소확인 중 오류 발생");
        }
    }

    @Transactional
    public ResponseDTO insertAddress(AddressBookResponseDTO addressBookResponseDTO){
        try{
            AddressBook addressBook = addressBookResponseDTO.toEntity();
            if(addressBook.isDefault()){
                addressBookMapper.updateAddressIsDefault(addressBook.getUserId());
            }

            int result = addressBookMapper.insertAddress(addressBook);
            if(result>0){
                return ResponseDTO.of("success",1355,"주소가 등록되었습니다.");
            }

            return ErrorResponseDTO.of(1356,"주소등록에 실패했습니다.");

        } catch (Exception e) {
            log.error("주소 등록 중 에러 발생 "+e.getMessage());
            return  ErrorResponseDTO.of(1333,"주소 등록 중 오류 발생");
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
                return ResponseDTO.of("success",1355,"주소가 수정되었습니다.");
            }

            return ErrorResponseDTO.of(1356,"주소수정에 실패했습니다.");

        } catch (Exception e) {
            log.error("주소 등록 중 에러 발생 "+e.getMessage());
            return  ErrorResponseDTO.of(1333,"주소 수정 중 오류 발생");
        }
    }
    //주소 삭제
    @Transactional
    public ResponseDTO deleteAddress(Long addressId,Long userId){
        try{
            if(addressId == 0 ){
                return ErrorResponseDTO.of(1356,"주소삭제에 실패했습니다.");
            }
            
          long id =  addressBookMapper.addressIsExist(addressId);
            if( userId != id){
                return ErrorResponseDTO.of(1356,"사용자가 일치 하지 않습니다.");
            }

            int result = addressBookMapper.deleteAddressById(addressId);
            
            if(result> 0){
                return ResponseDTO.of("success",1355,"주소가 삭제되었습니다.");
            }

            return ErrorResponseDTO.of(1356,"주소삭제에 실패했습니다.");

        } catch (Exception e) {
            log.error("주소 등록 중 에러 발생 "+e.getMessage());
            return  ErrorResponseDTO.of(1333,"주소 삭제 중 오류 발생");
        }
    }

}
