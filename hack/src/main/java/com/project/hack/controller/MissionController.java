package com.project.hack.controller;

import com.project.hack.dto.request.MissionRequestDto;
import com.project.hack.dto.response.MissionResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.repository.MissionRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionRepository missionRepository;
    private final MissionService missionService;

    /*
    //홈화면 큰카테고리 조회 매핑
    @GetMapping("/api/categories")
    public
     */

    @GetMapping("/api/missions")
    public List<MissionResponseDto> allMission() {
        return missionService.allMission();
    }

    @PostMapping("api/missions")
    public ResponseEntity createMission(@RequestBody MissionRequestDto missionRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {
            throw new CustomException(ErrorCode.Mission_NOT_FOUND);
        }
        return missionService.createMission(missionRequestDto, userDetails);
    }
}
