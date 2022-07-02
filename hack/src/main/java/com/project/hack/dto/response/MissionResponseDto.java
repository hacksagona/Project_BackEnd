package com.project.hack.dto.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class MissionResponseDto {

    private Long missionId;
    private Long userId;
    private String missionContent;
    //private enum category;
}
