package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.entity.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    public @ResponseBody String loginTest(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails) {
        System.out.println(authentication);
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        log.debug("authentication = {}", principal.getUser());
        log.debug("userDetails = {}", userDetails.getUser());
        return "login test";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOauthlogin(Authentication authentication,
                                          @AuthenticationPrincipal PrincipalDetails userDetails,
                                               @AuthenticationPrincipal OAuth2User oAuth2User) {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        log.debug("OAuth2User ATTRIBUTES  = {}", principal.getAttributes());
        log.debug("PrincipalDetails = {}", userDetails); //null
        log.debug("OAuth2User = {}", oAuth2User.getAttributes()); //principal == oAuth2User

        return "Oauth login test";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


    //OAuth 로그인을 해도 PrincipalDetails
    //일반 로그인을 해도 PrincipalDetails
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        System.out.println(principalDetails.getAttributes());
        System.out.println("user = " + user);
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    //스프링 시큐리티가 해당주소를 낚아 챔, SecurityConfig 파일 만든 후 작동 안함
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user); //회원가입
        return "redirect:/loginForm";
    }

    @Secured({"ROLE_ADMIN","ROLE_MANAGER"})
    @GetMapping("/info")
    public @ResponseBody String info() {
        return "개인정보";
    }

    //@PostAuthorize("hasRole('ROLE_MANAGER'))
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data() {
        return "데이터정보";
    }
}
