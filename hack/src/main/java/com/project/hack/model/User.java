package com.project.hack.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

    @Column
    private String p_number;

    @Column
    private String gender;

    @Column
    private String location;

    @Column
    private String birth;

    @Column(nullable = false)
    private String password;

    @Column
    private String profile_img;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private Long googleId;

    public User(String email, String name, String p_number, String gender, String location, String birth, String password, String profile_img) {
        this.email = email;
        this.name = name;
        this.p_number = p_number;
        this.gender = gender;
        this.location = location;
        this.birth = birth;
        this.password = password;
        this.profile_img= profile_img;
    }

    public User(String email, String name, String p_number, String gender, String location, String birth, String password, String profile_img,Long kakaoId) {
        this.email = email;
        this.name = name;
        this.p_number = p_number;
        this.gender = gender;
        this.location = location;
        this.birth = birth;
        this.password = password;
        this.profile_img = profile_img;
        this.kakaoId = kakaoId;
    }


    public User(String name, String email, String encodedPassword, Long kakaoId, String profile_img) {
        this.name = name;
        this.email = email;
        this.password = encodedPassword;
        this.kakaoId= kakaoId;
        this.profile_img = profile_img;
    }
}
