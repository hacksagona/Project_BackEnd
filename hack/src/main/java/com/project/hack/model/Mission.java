package com.project.hack.model;

import com.project.hack.dto.request.MissionRequestDto;
import com.project.hack.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Entity
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String missionContent;

    @Column(nullable = false)
    private String category;


    public Mission(MissionRequestDto missionRequestDto, UserDetailsImpl userDetails) {
        this.missionContent = missionRequestDto.getMissionContent();
        this.category = missionRequestDto.getCategory();
        this.userId = userDetails.getUser().getId();
    }

    public void fixMission(MissionRequestDto missionRequestDto) {
        this.category = missionRequestDto.getCategory();
        this.missionContent = missionRequestDto.getMissionContent();
    }
}

