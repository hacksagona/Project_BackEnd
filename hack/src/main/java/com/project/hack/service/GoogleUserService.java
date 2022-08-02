package com.project.hack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hack.dto.response.SocialUserInfoDto;
import com.project.hack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@Service
public class GoogleUserService {

    @Value("${auth.google.client-id}")
    private String googleClientId;

    @Value("${auth.google.client-secret}")
    private String googleClientSecret;

    @Value("${auth.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${cloud.aws.s3.profileimg}")
    private String profileImg;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // 구글 로그인

    public SocialUserInfoDto GoogleLogin(String code) throws JsonProcessingException {

        // 인가코드로 엑세스토큰 가져오기
        String accessToken = getAccessToken(code);

        // 엑세스토큰으로 유저정보 가져오기
        return getGoogleUserInfo(accessToken);
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
        String id = googleUserInfo.get("sub").asText();
        String email = googleUserInfo.get("email").asText();
        String nickname = googleUserInfo.get("name").asText();
        String profile_img = googleUserInfo.get("picture").asText();

        System.out.println("로그인 이용자 정보");
        System.out.println("구글 고유 ID : " + id);
        System.out.println("닉네임 : " + nickname);
        System.out.println("이메일 : " + email);
        System.out.println("프로필이미지 URL : " + profile_img);

        return SocialUserInfoDto.builder()
                .social("Google")
                .email(email)
                .nickname(nickname)
                .profile_img(profile_img).build();
    }
}