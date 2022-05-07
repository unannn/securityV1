package com.cos.security1.config.auth;

import com.cos.security1.entity.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// SecurityConfig 에서 loginProcessingUrl("/login"); 에 의해
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 메소드가 실행
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // 시큐리티 session 에는 Authentication 이 들어가고, Authentication 에는  UserDetails 가 들어감
    @Override
    public UserDetails loadUserByUsername(String username /*html에서의 아이디 입력태그 name*/) throws UsernameNotFoundException {
        User findUser = userRepository.findByUsername(username);
        if (findUser != null) {
            return new PrincipalDetails(findUser);
        }
        return null;
    }
}
