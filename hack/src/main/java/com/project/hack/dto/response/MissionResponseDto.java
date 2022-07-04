package com.project.hack.dto.response;


import com.project.hack.model.Mission;
import com.project.hack.security.UserDetailsImpl;
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


    public MissionResponseDto(Mission mission) {
        this.missionId = mission.getMissionId();
        this.missionContent = mission.getMissionContent();

    }
}
