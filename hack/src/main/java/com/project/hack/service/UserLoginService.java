package com.project.hack.service;


import com.project.hack.dto.response.SocialUserInfoDto;
import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.model.User;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserLoginService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${cloud.aws.s3.profileimg}")
    private String profileImg;


    public User finalLogin (SocialUserInfoDto userInfoDto){

        System.out.println(userInfoDto.getSocial() + " 유저확인 클래스 들어옴===================");
        String checkEmail = userInfoDto.getEmail();
        System.out.println("이메일 : "+ checkEmail);
        User user = userRepository.findByEmailAndSocial(checkEmail,userInfoDto.getSocial())
                .orElse(null);
        if (user == null) {
            // 회원가입
            System.out.println("회원정보 없는 회원임(새로운 회원!)");
            // username: kakao nickname
            String name = "유저"+Math.random()*100000;
//            String username = kakaoUserInfo.getNickname();
            String email = userInfoDto.getEmail();
            System.out.println("이메일 넣음 = " + email);

            // password: random UUID
            String password = UUID.randomUUID().toString();
            System.out.println("비밀번호 넣음 = " + password);

            String nickname = UUID.randomUUID().toString();
            System.out.println("닉네임 넣음 = " + nickname);

            String encodedPassword = passwordEncoder.encode(password);
            System.out.println("비밀번호 암호화  = " + encodedPassword);
//            String profile_img = kakaoUserInfo.getProfile_img();
            String profile_img = profileImg;
            System.out.println("프로필 넣음  = " + profile_img);
            String social = userInfoDto.getSocial();

            user = new User(name, nickname,email, encodedPassword, profile_img,social);
            userRepository.save(user);
        }
        System.out.println("유저정보 넣음");
        return user;
    }

    public UserResponseDto jwtTokenCreate(User user, HttpServletResponse response) {

        System.out.println("jwtTokenCreate 클래스 들어옴");

        UserDetails userDetails = new UserDetailsImpl(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //여기까진 평범한 로그인과 같음
        System.out.println("강제로그인 시도까지 함");
        //여기부터 토큰 프론트에 넘기는것

        UserDetailsImpl userDetails1 = ((UserDetailsImpl) authentication.getPrincipal());

        System.out.println("userDetails1 : " + userDetails1.toString());

        final String token = JwtTokenUtils.generateJwtToken(userDetails1);

        System.out.println("token값:" +"BEARER " + token);
        response.addHeader("Authorization", "BEARER " + token);

        return UserResponseDto.builder()
                .userId(user.getId())
                .isNewUser(user.isNewUser())
                .picChange(user.isPicChange())
                .email(user.getEmail())
                .profile_img(user.getProfile_img()).build();
    }
}
