package com.project.hack.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hack.dto.response.SocialUserInfoDto;
import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class NaverUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Value("${auth.naver.client-id}")
    private String naverClientKId;
    /**
     * 네이버는 타 소셜과 다르게 secret키가 필요하다
     */
    @Value("${auth.naver.client-secret}")
    private String naverClientSecret;

    @Value("${auth.naver.redirect-uri}")
    private String naverRedirectUri;

    @Value("${cloud.aws.s3.profileimg}")
    private String profileImg;

    @Transactional
    public UserResponseDto naverLogin(String code, HttpServletResponse response, String state) throws JsonProcessingException {
        // 1. "인가 코드"로 "액세스 토큰" 요청

        System.out.println("인가 코드 : " + code);
        System.out.println("naverClientKId : " +naverClientKId);
        System.out.println("naverClientSecret : " +naverClientSecret);
        System.out.println("naverRedirectUri : " +naverRedirectUri);
        String accessToken = getAccessToken(code, state);
        System.out.println("엑세스 토큰: " + accessToken);

        // 2. 토큰으로 카카오 API 호출
        SocialUserInfoDto naverUserInfo = getNaverUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User naverUser = registerKakaoUserIfNeeded(naverUserInfo);

        // 4. 강제 로그인 처리
        jwtTokenCreate(naverUser, response);

        return UserResponseDto.builder()
                .userId(naverUser.getId())
                .isNewUser(naverUser.isNewUser())
                .isTutorial(naverUser.isTutorial())
                .email(naverUser.getEmail())
                .nickname(naverUser.getNickname())
                .profile_img(naverUser.getProfile_img()).build();
    }

    private String getAccessToken(String code, String state) throws JsonProcessingException {
        System.out.println("getAccessToken 들어옴");
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성 (네이버는 secret key가 필요하다)
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", naverClientKId);
        body.add("client_secret", naverClientSecret);
        body.add("redirect_uri", naverRedirectUri);
        body.add("code", code);
        body.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://nid.naver.com/oauth2.0/token",
                    HttpMethod.POST,
                    naverTokenRequest,
                    String.class
            );

            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            String responseBody = response.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String access_token = jsonNode.get("access_token").asText();
            String refresh_token = jsonNode.get("refresh_token").asText();
            System.out.println("access token : " + access_token);
            System.out.println("refresh token : " + refresh_token);

            return access_token;
        } catch (HttpClientErrorException e) {
            throw new CustomException(ErrorCode.COMMON_BAD_REQUEST_400);
        }
    }

    private SocialUserInfoDto getNaverUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("헤더까지는 받음 헤더 : " + headers);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.POST,
                naverUserInfoRequest,
                String.class
        );
        System.out.println("유저정보 받는 post는 통과함");

        String responseBody = response.getBody();
        System.out.println("responseBody : "+responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        System.out.println("유저정보 작성 시작");
        String email = jsonNode.get("response").get("email").asText();
        System.out.println("email : " + email);

        System.out.println("네이버 사용자 정보: "+  email);
        return SocialUserInfoDto.builder()
                .social("Naver")
                .email(email).build();
    }

    private User registerKakaoUserIfNeeded(SocialUserInfoDto UserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        System.out.println("네이버유저확인 클래스 들어옴===================");
        String naverEmail = UserInfo.getEmail();
        User naverUser = userRepository.findByEmailAndSocial(naverEmail, UserInfo.getSocial())
                .orElse(null);
        if (naverUser == null) {
            // 회원가입
            System.out.println("회원정보 없는 회원임(새로운 회원!)");
            // username: kakao nickname
            String name = UUID.randomUUID().toString();
            System.out.println("네임 넣음 = " + name);
//            String username = kakaoUserInfo.getNickname();
            String email = UserInfo.getEmail();
            System.out.println("이메일 넣음 = " + email);

            // password: random UUID
            String password = UUID.randomUUID().toString();
            System.out.println("비밀번호 넣음 = " + password);

            String nickname = UUID.randomUUID().toString();
            System.out.println("닉네임 넣음 = " + nickname);

            String encodedPassword = passwordEncoder.encode(password);
            System.out.println("비밀번호 암호화  = " + encodedPassword);

            String profile_img = profileImg;
            System.out.println("프로필 넣음  = " + profile_img);
            String social = UserInfo.getSocial();

            naverUser = new User(name, nickname,email, encodedPassword, profile_img,social);
            userRepository.save(naverUser);
        }
        System.out.println("네이버 유저정보 넣음");
        return naverUser;
    }

    private void jwtTokenCreate(User user, HttpServletResponse response) {

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

    }
}