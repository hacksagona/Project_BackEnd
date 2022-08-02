package com.project.hack.dto.response;


import com.project.hack.model.Mission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MissionResponseDto {

    private Long missionId;
    private Long userId;
    private String category;
    private String missionContent;
    private boolean missionState;


    public MissionResponseDto(Mission mission) {
        this.missionId = mission.getMissionId();
        this.userId = mission.getUserId();
        this.missionState = mission.getMissionState();
        this.missionContent = mission.getMissionContent();
        this.category =mission.getCategory();
    }

}
