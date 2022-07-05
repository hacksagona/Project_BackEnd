package com.project.hack.controller;

import com.project.hack.dto.request.MissionRequestDto;
import com.project.hack.dto.response.MissionResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Mission;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    //메인페이지 큰카테고리
    @GetMapping("/api/categories")
    public MissionResponseDto homeCategories(@RequestParam String category, UserDetailsImpl userDetails) {
        if (userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}

        return new MissionResponseDto(category); //변수 인라인화
    }

    //목표 리스트 조회
    @GetMapping("/api/missions")
    public List<MissionResponseDto> getMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {throw new CustomException(ErrorCode.MISSION_NOT_FOUND);}

        return missionService.getMission();
    }

    //목표 생성
    @PostMapping("api/missions")
    public Mission createMission(@RequestBody MissionRequestDto missionRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return missionService.createMission(missionRequestDto, userDetails);
    }

    //목표 수정
    @PutMapping("/api/missions/{missionId}")
    public ResponseEntity editMission(@RequestBody MissionRequestDto missionRequestDto, @PathVariable Long missionId) {

        return missionService.editMission(missionRequestDto, missionId);
    }

    //목표 삭제
    @DeleteMapping("/api/missions/{missionId}")
    public Long deleteMission(@PathVariable Long missionId) {

        return missionService.deleteMission(missionId);
    }

}
