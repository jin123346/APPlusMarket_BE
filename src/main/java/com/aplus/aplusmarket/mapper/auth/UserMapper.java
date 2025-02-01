package com.aplus.aplusmarket.mapper.auth;

import com.aplus.aplusmarket.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {


    Optional<User> selectUserByUid(@Param("uid") String uid);
    Optional<User> selectUserByEmail(@Param("email") String email);
    Optional<User> selectUserByHp(@Param("hp") String hp);

    void insertUser(User user);



}
