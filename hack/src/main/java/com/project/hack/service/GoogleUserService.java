package com.project.hack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hack.dto.response.SocialUserInfoDto;
import com.project.hack.model.User;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;



import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GoogleUserService {

    @Value("${auth.google.client-id}")
    private String googleClientId;

    @Value("${auth.google.client-secret}")
    private String googleClientSecret;

    @Value("${auth.google.redirect-uri}")
    private String googleRedirectUri;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // 구글 로그인

    public void GoogleLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        // 인가코드로 엑세스토큰 가져오기
        String accessToken = getAccessToken(code);

        // 엑세스토큰으로 유저정보 가져오기
        SocialUserInfoDto googleUserInfo = getGoogleUserInfo(accessToken);


        User googleUser = registerGoogleUserIfNeeded(googleUserInfo);

        // 4. 강제 로그인 처리
        jwtTokenCreate(googleUser, response);
    }

    // 인가코드로 엑세스토큰 가져오기
    public String getAccessToken(String code) throws JsonProcessingException {
        System.out.println("getAccessToken 들어옴");
        // 헤더에 Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        System.out.println("code = " + code);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 바디에 필요한 정보 담기
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", googleClientId);
        body.add("client_secret", googleClientSecret);
        body.add("code", code);
        body.add("redirect_uri", googleRedirectUri);
        body.add("grant_type", "authorization_code");
        System.out.println("바디 : " + body);

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleToken = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST, googleToken,
                String.class
        );
        System.out.println("HTTP에 요청 보냄");

        // response에서 엑세스토큰 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseToken = objectMapper.readTree(responseBody);
        String accessToken = responseToken.get("access_token").asText();
        System.out.println("access token : " + accessToken);

        return accessToken;
    }

    // 엑세스토큰으로 유저정보 가져오기
    public SocialUserInfoDto getGoogleUserInfo(String accessToken) throws JsonProcessingException {

        // 헤더에 엑세스토큰 담기, Content-type 지정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer" + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("헤더까지는 받음 헤더 : " + headers);

        // POST 요청 보내기
        HttpEntity<MultiValueMap<String, String>> googleUser = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo",
                HttpMethod.POST, googleUser,
                String.class
        );
        System.out.println("유저정보 받는 post는 통과함");

        // response 에서 유저정보 가져오기
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode googleUserInfo = objectMapper.readTree(responseBody);

        // 유저정보 작성
        Long id = googleUserInfo.get("sub").asLong();
        String email = googleUserInfo.get("email").asText();
        String nickname = googleUserInfo.get("name").asText();
        String profile_image = googleUserInfo.get("picture").asText();

        System.out.println("로그인 이용자 정보");
        System.out.println("구글 고유 ID : " + id);
        System.out.println("닉네임 : " + nickname);
        System.out.println("이메일 : " + email);
        System.out.println("프로필이미지 URL : " + profile_image);

        return SocialUserInfoDto.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .profile_img(profile_image).build();
    }


    private User registerGoogleUserIfNeeded(SocialUserInfoDto googleUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        System.out.println("구글유저확인 클래스 들어옴");
        Long googleId = googleUserInfo.getId();
        User googleUser = userRepository.findByGoogleId(googleId)
                .orElse(null);
        if (googleUser == null) {
            // 회원가입
            // username: kakao nickname
            String name = googleUserInfo.getNickname();
            System.out.println("닉네임 넣음 = " + name);
//            String username = kakaoUserInfo.getNickname();
            String email = googleUserInfo.getEmail();
            System.out.println("이메일 넣음 = " + email);

            // password: random UUID
            String password = UUID.randomUUID().toString();
            System.out.println("비밀번호 넣음 = " + password);

            String encodedPassword = passwordEncoder.encode(password);
            System.out.println("비밀번호 암호화  = " + encodedPassword);
            String profile_img = googleUserInfo.getProfile_img();
            System.out.println("프로필 넣음  = " + profile_img);

            googleUser = User.builder()
                    .name(name)
                    .email(email)
                    .password(encodedPassword)
                    .googleId(googleId)
                    .profile_img(profile_img)
                    .build();
            userRepository.save(googleUser);
        }
        System.out.println("구글 유저정보 넣음");
        return googleUser;
    }

    private void jwtTokenCreate(User googleUser, HttpServletResponse response) {

        System.out.println("jwtTokenCreate 클래스 들어옴");

        UserDetails userDetails = new UserDetailsImpl(googleUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //여기까진 평범한 로그인과 같음
        System.out.println("강제로그인 시도까지 함");
        //여기부터 토큰 프론트에 넘기는것

        UserDetailsImpl userDetails1 = ((UserDetailsImpl) authentication.getPrincipal());

        System.out.println("userDetails1 : " + userDetails1.toString());

        final String token = JwtTokenUtils.generateJwtToken(userDetails1);

        System.out.println("token값:" + token);
        response.addHeader("Authorization", "BEARER" + " " + token);

    }
}