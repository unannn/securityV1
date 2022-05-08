package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록이 됩니다.
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //non-browser clients 만을 위한 서비스하면 csrf 를 disable 하여도 좋다고 함, 서버에 인증정보를 저장하지 않기 때문

        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() //인증만 되면 들어갈 수 있는 주소!!
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()

                //권한이필요한 페이지로 접근시 로그인페이지로 이동
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") //login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
                .defaultSuccessUrl("/") //로그인이 완료되면 이돌될 url

                /**
                 * //구글 로그인이 완료된 뒤의 후처리 필요.
                 * 1. 코드 받기(인증),
                 * 2. 엑세스 토큰(권한),
                 * 3. 사용자 프로필정보를 가져오고
                 * 4. 그 정보를 토대로 회원가입을 진행시키기도 함
                 * 4-2 (이메일, 전화번호, 이름, 아이디 등) 만약 쇼핑몰이라면 집주소, 백화점 몰, vip등급, 일반등급
                 */
                .and()
                .oauth2Login()
                .loginPage("/loginForm")  //구글 로그인을 하면 엑세스 토큰 + 사용자 프로필을 한번에 받음
                .userInfoEndpoint()
                .userService(principalOauth2UserService);


    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
