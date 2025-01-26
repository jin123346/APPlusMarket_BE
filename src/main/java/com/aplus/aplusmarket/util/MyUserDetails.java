package com.aplus.aplusmarket.util;


import com.aplus.aplusmarket.entity.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUserDetails implements UserDetails , OAuth2User {

    private User user;
    private Map<String,Object> attributes;
    private String accessToken;

    public MyUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUid();
    }
    @Override
    public boolean isAccountNonExpired() {
        //계정 만료 여부(true: 만료안됨, false:만료)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //계정 잠김 여부(true : 잠김아님, false : 잠김)
        return  !user.getStatus().equals("Block");
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 비밀번호 만료 여부(true : 만료안됨, false : 만료)
        return true;
    }

    @Override
    public boolean isEnabled() {
        //계정 활성 여부(true : 활성화, false : 비활성화)
        return user.getStatus().equals("Active");
    }
}
