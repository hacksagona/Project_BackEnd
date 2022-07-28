package com.project.hack.dto.response;

import com.project.hack.model.User;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String name;

    private Long userId;
    private String nickname;
    private String profile_img;
    private boolean isNewUser;
    private boolean picChange;



    public UserResponseDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public UserResponseDto(Long userId, String nickname, String profile_img) {
        this.userId = userId;
        this.nickname = nickname;
        this.profile_img = profile_img;
    }

    public UserResponseDto(String email, String name, String nickname) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
    }
}