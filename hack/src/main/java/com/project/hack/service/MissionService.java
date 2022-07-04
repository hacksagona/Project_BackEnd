package com.project.hack.service;

import com.project.hack.dto.request.MissionRequestDto;
import com.project.hack.dto.response.MissionResponseDto;
import com.project.hack.model.Mission;
import com.project.hack.repository.MissionRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    public List<MissionResponseDto> allMission() {
        List<MissionResponseDto> missionResponseDtoList = new ArrayList<>();
        List<Mission> missionList = missionRepository.findAll();
        for (int i = 0; i <= missionList.size(); i++) {
            Mission mission = missionList.get(i);
            MissionResponseDto responseDto = new MissionResponseDto(mission);
            missionResponseDtoList.add(responseDto);
        }
        return missionResponseDtoList;
    }

    public ResponseEntity createMission(MissionRequestDto missionRequestDto, UserDetailsImpl userDetails) {
    }
}
