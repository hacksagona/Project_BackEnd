package com.project.hack.service;


import com.project.hack.dto.request.SignupRequestDto;
import com.project.hack.dto.request.UserRequestDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.User;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void registerUser(SignupRequestDto requestDto) {
        // 회원 ID 중복 확인
//        String email = requestDto.getEmail();
//
//        if(userRepository.findByEmail(email).isPresent()){
//            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
//        }
//        String name = requestDto.getName();
//        String p_number =requestDto.getP_number();
//        String gender = requestDto.getGender();
//        String location = requestDto.getLocation();
//        String birth = requestDto.getBirth();
//        String profile_img = requestDto.getProfile_img();
//
//        // 패스워드 암호화
//        String password = passwordEncoder.encode(requestDto.getPassword());
//
//
//
//        User user = new User(email, name, p_number, gender, location, birth, password,profile_img);
//        userRepository.save(user);
    }

    public boolean checkEmail(SignupRequestDto requestDto){
        String email = requestDto.getEmail();
        return (!userRepository.findByEmail(email).isPresent());
    }

    public boolean checkNickname(SignupRequestDto requestDto){
        String nickname = requestDto.getNickname();
        if(nickname ==null|| nickname.equals("")){
            System.out.println("중복 체크 시도하는 닉네임이 빈값임");
            throw new CustomException(ErrorCode.NICKNAME_NOT_FOUND);
        }
        System.out.println("중복 체크 시도하는 닉네임 : "+ nickname);
        return (!userRepository.findByNickname(nickname).isPresent());
    }

    @Transactional
    public User putNickname(UserRequestDto requestDto, UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        System.out.println("User = " + user);
        String nickname = requestDto.getNickname();
        System.out.println("nickname = " + nickname);
        if(nickname ==null || nickname.equals("")){
            throw new CustomException(ErrorCode.NICKNAME_NOT_FOUND);
        }

        if(userRepository.findByNickname(nickname).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_NICKNAME);
        }
        System.out.println("유저닉네임 : " + user.getNickname());
        user.updateNickname(nickname);
        System.out.println("수정 후 유저 닉네임 : " + user.getNickname());
        userRepository.save(user);
        System.out.println("저장완료");
        return user;
    }


    @Transactional
    //마이페이지 수정하기
    public Long updateMyInfo(Long userId, UserRequestDto userRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NullPointerException("유저가 존재하지 않습니다")
        );

        user.updateUser(userId, userRequestDto);
        return user.getId();
    }
}
