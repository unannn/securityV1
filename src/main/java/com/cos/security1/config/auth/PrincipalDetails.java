package com.cos.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인 짆앵이 완료가 되면 session 을 만들어줍니다. (Security ContextHolder)
// 오브젝스 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 됨
// User 오브젝트 타입 => UserDetails 타입 객체

//시큐리티 세션 영역이 있고, 여기 들어갈 수 있는 객체는 Authentication
//Authentication 에 들어가 유저 정보는 UserDetails

import com.cos.security1.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class PrincipalDetails implements UserDetails {

    private User user;

    PrincipalDetails(User user) {
        this.user = user;
    }

    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        user.getRole();
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //사이트에서 1년동안 회원이 로그인을 한해서 휴면 계정으로 하기로함
        // 현재시간 - 최근 로그인 시간이 1년을 초과하면 return false
        return true;
    }
}
