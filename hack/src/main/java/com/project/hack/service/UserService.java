package com.project.hack.service;


import com.project.hack.dto.request.SignupRequestDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.User;
import com.project.hack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void registerUser(SignupRequestDto requestDto) {
        // 회원 ID 중복 확인
        String email = requestDto.getEmail();

        if(userRepository.findByEmail(email).isPresent()){
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        String name = requestDto.getName();
        String p_number =requestDto.getP_number();
        String gender = requestDto.getGender();
        String location = requestDto.getLocation();
        String birth = requestDto.getBirth();

        // 패스워드 암호화
        String password = passwordEncoder.encode(requestDto.getPassword());



        User user = new User(email, name, p_number, gender, location, birth, password);
        userRepository.save(user);
    }

    public boolean checkEmail(SignupRequestDto requestDto){
        String email = requestDto.getEmail();
        return (!userRepository.findByEmail(email).isPresent());
    }


}
