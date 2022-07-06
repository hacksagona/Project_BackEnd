package com.project.hack.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDto {
    private String email;
    private String password;
    //    private String passwordCheck;
    private String name;
    private String nickname;

    private String profile_img;
}