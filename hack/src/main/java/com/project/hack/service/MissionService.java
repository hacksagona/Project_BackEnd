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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Setter
@Getter
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    //목표 리스트 조회
    public List<MissionResponseDto> getMission( ) {
        List<MissionResponseDto> missionResponseDtoList = new ArrayList<>();
        List<Mission> missionList = missionRepository.findAll();
        for (Mission mission : missionList) {
            /* for (int i = 0; i < missionList.size(); i++) {
                Mission mission = missionList.get(i); */
            MissionResponseDto responseDto = new MissionResponseDto(mission);
            missionResponseDtoList.add(responseDto);
        }
        return missionResponseDtoList;
    }

    //목표 생성
    public Mission createMission(MissionRequestDto missionRequestDto, UserDetailsImpl userDetails) {
        Mission mission = new Mission(missionRequestDto, userDetails);

        return missionRepository.save(mission);
    }

    //목표 수정
    @Transactional//=>수정하는 도중 다른 유저들의 접근에 대한 에러 방지용
    public Long editMission(MissionRequestDto missionRequestDto, Long missionId) {
        Mission mission = missionRepository.findByMissionId(missionId);
        mission.fixMission(missionRequestDto);
        return mission.getMissionId();
    }

    //목표 삭제
    @Transactional
    public Long deleteMission(Long missionId) {
        missionRepository.deleteById(missionId);
        return missionId;
    }
}
