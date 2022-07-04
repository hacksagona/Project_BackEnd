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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class KakaoUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${auth.kakao.client-id}")
    private String kakaoClientKId;

    @Value("${auth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Transactional
    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청
        System.out.println("kakaoClientKId = " + kakaoClientKId);
        System.out.println("kakaoRedirectUri = " + kakaoRedirectUri);
        System.out.println("인가 코드 : " + code);
        String accessToken = getAccessToken(code);
        System.out.println("엑세스 토큰: " + accessToken);

        // 2. 토큰으로 카카오 API 호출
        SocialUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. 강제 로그인 처리
        jwtTokenCreate(kakaoUser, response);
    }

    private String getAccessToken(String code) throws JsonProcessingException {
        System.out.println("getAccessToken 들어옴");
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoClientKId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);
        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        System.out.println("HTTP에 요청 보냄");

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private SocialUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("헤더까지는 받음 헤더 : " + headers);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );
        System.out.println("유저정보 받는 post는 통과함");

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        System.out.println("id = " + id);

        String name = jsonNode.get("properties")
                .get("nickname").asText();

        String profile_img = jsonNode.get("properties")
                .get("profile_image").asText();

        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        System.out.println("카카오 사용자 정보: " + id + ", " + name + ","+profile_img+", "+ email);
        return new SocialUserInfoDto(id, name,profile_img, email);
    }

    private User registerKakaoUserIfNeeded(SocialUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        System.out.println("카톡유저확인 클래스 들어옴===================");
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // 회원가입
            // username: kakao nickname
            String name = kakaoUserInfo.getNickname();
            System.out.println("닉네임 넣음 = " + name);
//            String username = kakaoUserInfo.getNickname();
            String email = kakaoUserInfo.getEmail();
            System.out.println("이메일 넣음 = " + email);

            // password: random UUID
            String password = UUID.randomUUID().toString();
            System.out.println("비밀번호 넣음 = " + password);

            String nickname = UUID.randomUUID().toString();
            System.out.println("닉네임 넣음 = " + password);

            String encodedPassword = passwordEncoder.encode(password);
            System.out.println("비밀번호 암호화  = " + encodedPassword);
            String profile_img = kakaoUserInfo.getProfile_img();
            System.out.println("프로필 넣음  = " + profile_img);

            kakaoUser = new User(name, nickname,email, encodedPassword, kakaoId, profile_img);
            userRepository.save(kakaoUser);
        }
        System.out.println("카카오톡 유저정보 넣음");
        return kakaoUser;
    }

    private void jwtTokenCreate(User kakaoUser, HttpServletResponse response) {

        System.out.println("jwtTokenCreate 클래스 들어옴");

        UserDetails userDetails = new UserDetailsImpl(kakaoUser);
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

    }
}