package com.project.hack.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MissionRequestDto {

    private boolean missionState;
    private String missionContent;
}
