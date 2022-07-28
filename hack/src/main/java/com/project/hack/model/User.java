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

    @Column(nullable = false)
    private String social;

    @Column(nullable = false)
    private boolean isNewUser;

    @Column(nullable = false)
    private boolean picChange;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Post> posts;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments;


    public User(String name, String nickname,String email, String encodedPassword,String profile_img, String social) {
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = encodedPassword;
        this.profile_img = profile_img;
        this.isNewUser = true;
        this.picChange = false;
        this.social = social;
    }

    public void updateNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateIsNewUser(){this.isNewUser = false;}
    public void updatePicChange(){this.picChange = true;}


    // 마이페이지 업데이트
    public void updateUser(Long userId, UserRequestDto userRequestDto) {
        this.id = userId;
        this.nickname = userRequestDto.getNickname();
        this.profile_img = userRequestDto.getProfile_img();
    }

    public void updateProfileImg(String profile_img) {
        this.profile_img = profile_img;
    }
}
