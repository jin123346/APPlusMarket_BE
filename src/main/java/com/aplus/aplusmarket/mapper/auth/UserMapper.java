package com.aplus.aplusmarket.mapper.auth;

/*
     2025.02.05 하진희 myinfo update - updateUserById
 */
import com.aplus.aplusmarket.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper
public interface UserMapper {


    Optional<User> selectUserByUid(@Param("uid") String uid);
    Optional<User> selectUserById(@Param("id") long id);
    Optional<User> selectUserByEmail(@Param("email") String email);
    Optional<User> selectUserByHp(@Param("hp") String hp);


    String selectUserByNameAndEmail(@Param("name") String name, @Param("email") String email);
    Long selectUserByUidAndEmail(@Param("uid") String uid, @Param("email") String email);

    int updateUserById(@Param("id") long id
            , @Param("email") String email
            , @Param("hp") String hp
            , @Param("birthday") LocalDateTime birthday
            , @Param("nickName") String nickName);
    void insertUser(User user);

    int updatePassById(@Param("id") Long id, @Param("password") String password);



}
