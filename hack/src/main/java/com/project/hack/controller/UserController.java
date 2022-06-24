package com.project.hack.controller;

import com.project.hack.dto.request.SignupRequestDto;
import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.model.User;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        System.out.println("name : " + user.getName());
        return new UserResponseDto(user.getEmail(), user.getName());
    }

    @PostMapping("/user/signup/checkEmail")
    public boolean checkEmail(@RequestBody SignupRequestDto requestDto) {

        return userService.checkEmail(requestDto);
    }
}
