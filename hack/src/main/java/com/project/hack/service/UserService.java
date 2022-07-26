package com.project.hack.service;


import com.project.hack.dto.request.SignupRequestDto;
import com.project.hack.dto.request.UserRequestDto;
import com.project.hack.dto.response.PhotoDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Photo;
import com.project.hack.model.User;
import com.project.hack.repository.PhotoRepository;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;


@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final AwsService awsService;

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

    public boolean updateIsNewUser(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        user.updateIsNewUser();
        userRepository.save(user);
        System.out.println("isNewUser 수정 후 : " +user.isNewUser());
        return user.isNewUser();
    }

    public boolean updateIsTutorial(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        user.updateIsTutorial();
        userRepository.save(user);
        System.out.println("isTutorial 수정 후 : " +user.isTutorial());
        return user.isTutorial();
    }

    public void changeProfile(List<MultipartFile> multipartFile, UserDetailsImpl userDetails) {

        System.out.println("프사 수정 시도");
        System.out.println("들어온 사진 : " + multipartFile);

        if(multipartFile == null) throw new NullPointerException("파일이 존재하지 않습니다");
        User user = userDetails.getUser();
        String url = user.getProfile_img();
        if (photoRepository.findByUrl(url).isPresent()) {
            Photo photo = photoRepository.findByUrl(url).orElseThrow(
                    () -> new NullPointerException("사진이 존재하지 않습니다")
            );
            String filename = photo.getKey1();
            awsService.deleteFile(filename);
            photoRepository.delete(photo);
        }
        PhotoDto photoDto = awsService.uploadFile(multipartFile);
        String profile_img = photoDto.getPath();
        System.out.println("프사 : " + profile_img);
        user.updateProfileImg(profile_img);
        userRepository.save(user);
        System.out.println("프사 수정후 저장성공");
    }
}
