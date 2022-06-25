package com.project.hack.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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
    private String p_number;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String birth;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String profile_img;

    @Column(unique = true)
    private Long kakaoId;

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
}
