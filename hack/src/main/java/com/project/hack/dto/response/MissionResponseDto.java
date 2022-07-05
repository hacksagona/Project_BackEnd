package com.project.hack.dto.response;


import com.project.hack.dto.request.MissionRequestDto;
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
    private String category;

    public MissionResponseDto(Mission mission) {
        this.missionContent = mission.getMissionContent();
        this.category = mission.getCategory();
    }

    public MissionResponseDto(String category) {
        this.category = category;
    }

}
