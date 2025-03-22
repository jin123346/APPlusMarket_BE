package com.aplus.aplusmarket.mapper.auth;


import com.aplus.aplusmarket.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    List<AddressBook> findByUserId(Long userId);
    long insertAddress(AddressBook addressBook);
    void updateAddressIsDefault(Long userid);
    int updateAddressForModify(AddressBook addressBook);
    int deleteAddressById(Long id);
    Long addressIsExist(Long id);
}
