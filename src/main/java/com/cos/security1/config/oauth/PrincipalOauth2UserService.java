package com.cos.security1.config.oauth;

import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.swing.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    //구글로 부터 받은 userRequest 데이터 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("user Request = {}", userRequest);
        log.debug("ClientRegistration = {}", userRequest.getClientRegistration()); //registrationId로 어떤 OAuth로 로그인 했는지 확인가능.
        log.debug("AccessToken = {}", userRequest.getAccessToken().getTokenValue()); //토큰 정보

        // 구글 로그인 버튼 클릭-> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Client 라이브러리가 받음) -> AccessToken 요청
        // userRequest 정보 -> 회원 프로필을 받아야함(loadUser함수를 통해)
        // 결론 : loadUser를 통해 구글로 부터 회원 프로필을 받아준다.
        log.debug("loadUser(userRequest).getAttributes = {}", super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        return super.loadUser(userRequest);
    }
}
