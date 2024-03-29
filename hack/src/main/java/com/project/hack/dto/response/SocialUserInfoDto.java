package com.project.hack.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class SocialUserInfoDto {
    private String social;
    private String nickname;
    private String profile_img;
    private String email;

}
