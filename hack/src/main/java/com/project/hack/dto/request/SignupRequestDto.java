package com.project.hack.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequestDto {
    private String name;
    private String email;
    private String password;
    private String gender;
    private String location;
    private String p_number;
    private String birth;
}