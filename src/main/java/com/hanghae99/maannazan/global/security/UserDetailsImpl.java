package com.hanghae99.maannazan.global.security;

import org.springframework.security.core.GrantedAuthority;
import com.hanghae99.maannazan.domain.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class UserDetailsImpl implements UserDetails {

    private final User user;
    private final String nickName;

    public UserDetailsImpl(User user, String nickName) {
        this.user = user;
        this.nickName = nickName;
    }




    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return null;}

    @Override
    public String getUsername() {
        return this.nickName;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

