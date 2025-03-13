package com.aplus.aplusmarket.util;

import com.aplus.aplusmarket.entity.User;
import com.aplus.aplusmarket.mapper.auth.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        Optional<User> opt = userMapper.selectUserByUid(uid);

        if(opt.isPresent()) {
            User myUser = opt.get();
            return new MyUserDetails(myUser);
        }

        throw new UsernameNotFoundException("User not found  with uid: " + uid);
    }
}
