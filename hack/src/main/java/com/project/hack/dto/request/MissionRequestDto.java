package com.project.hack.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class MissionRequestDto {

    private Long missionId;
    private String missionContent;
    //private enum category;
}
