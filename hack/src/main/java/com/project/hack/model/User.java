package com.project.hack.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.hack.dto.request.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column
    private String profile_img;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private Long googleId;

    @Column(nullable = false)
    private boolean isNewUser;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Post> posts;


    public User(String name, String nickname,String email, String encodedPassword, Long kakaoId, String profile_img) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = encodedPassword;
        this.kakaoId= kakaoId;
        this.profile_img = profile_img;
        this.isNewUser = true;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateIsNewUser(){this.isNewUser = false;}


    // 마이페이지 업데이트
    public void updateUser(Long userId, UserRequestDto userRequestDto) {
        this.id = userId;
        this.nickname = userRequestDto.getNickname();
        this.profile_img = userRequestDto.getProfile_img();
    }
}
