package com.project.hack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.hack.dto.request.SignupRequestDto;
import com.project.hack.dto.request.UserRequestDto;
import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.User;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.GoogleUserService;
import com.project.hack.service.KakaoUserService;
import com.project.hack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    private final KakaoUserService kakaoUserService;
    private final GoogleUserService googleUserService;




    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity registerUser(@RequestBody SignupRequestDto requestDto) {

            userService.registerUser(requestDto);
            return new ResponseEntity("회원가입 성공!", HttpStatus.OK);
    }

    @GetMapping("/user/auth")
    public UserResponseDto islogin(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("islogin 시작");
        User user = userDetails.getUser();
        System.out.println("email : " + user.getEmail());
        System.out.println("nickname : " + user.getNickname());
        return new UserResponseDto(user.getEmail(), user.getName(),user.getId(),user.getNickname(),user.getProfile_img());
    }

    @GetMapping("/oauth/kakao/callback")
    public UserResponseDto kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        try { // 회원가입 진행 성공시 true
            return kakaoUserService.kakaoLogin(code, response);
        }catch (Exception e){ // 에러나면 false
            System.out.println("카톡 로그인 성공 못함!");
            throw new CustomException(ErrorCode.INVALID_LOGIN_ATTEMPT);
        }
    }

    @GetMapping("/oauth/google/callback")
    public ResponseEntity GoogleLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {

        try { // 회원가입 진행 성공시 true
            System.out.println("구글 로그인 시도");
            UserResponseDto userResponseDto = googleUserService.GoogleLogin(code, response);
            return new ResponseEntity(userResponseDto,HttpStatus.OK);
        }catch (Exception e){ // 에러나면 false
            System.out.println("구글 로그인 성공 못함!");
            throw new CustomException(ErrorCode.INVALID_LOGIN_ATTEMPT);
        }
    }

    @PutMapping("/user/update/nickname")
    public Long loginNickname(@RequestBody UserRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println("닉넴 수정 시도");
        return userService.putNickname(requestDto,userDetails).getId();

    }
    @PutMapping("/user/update/isNewUser")
    public boolean updateIsNewUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return !userService.updateIsNewUser(userDetails);
    }

    @PostMapping("/user/signup/checkEmail")
    public boolean checkEmail(@RequestBody SignupRequestDto requestDto) {

        return userService.checkEmail(requestDto);
    }

    @PostMapping("/user/signup/checkNickname")
    public boolean checkNickname(@RequestBody SignupRequestDto requestDto) {

        return userService.checkNickname(requestDto);
    }


    // ===================== 마이 페이지 =======================

    // 1. 마이페이지 불러오기
    @GetMapping("api/mypage/{userId}")
    public UserResponseDto getMypage(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 존재하지 않습니다")
        );

        return new UserResponseDto(user.getId(), user.getNickname(), user.getProfile_img());
    }

    // 2. 마이페이지 수정하기
    @PutMapping("/api/mypage/{userId}")
    public User updateMyInfo(@PathVariable Long userId,
                             @RequestBody UserRequestDto userRequestDto) {
         userService.updateMyInfo(userId, userRequestDto);
         return userRepository.findById(userId).orElseThrow(
                 () -> new IllegalArgumentException("유저가 존재하지 않습니다")
         );
    }









}
