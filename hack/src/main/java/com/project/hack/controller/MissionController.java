package com.project.hack.controller;

import com.project.hack.dto.request.MissionRequestDto;
import com.project.hack.dto.response.MissionResponseDto;
import com.project.hack.enums.Category;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Mission;
import com.project.hack.repository.MissionRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;
    private final MissionRepository missionRepository;

    //메인페이지 큰카테고리
    @GetMapping("/api/categories")
    public Mission getCategories(Mission mission) {
        return missionRepository.findByCategory(mission.getCategory());
    }

    /*enum 구현중
    //메인페이지 큰카테고리
    @GetMapping("/api/categories")
    public Category getCategories(Category category) {
        List<Mission> missionList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(Category.valueOf(category.showCategory()));
        missionList = categoryList
        return ;
    }*/

    //목표 전체 리스트 조회
    @GetMapping("/api/category/missions")
    public List<MissionResponseDto> getMission(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {throw new CustomException(ErrorCode.MISSION_NOT_FOUND);}
        return missionService.getMission(userDetails);
    }

    //목표 완료/미완료 리스트 조회
    @GetMapping("/api/category/missions/{missionState}")
    public List<Mission> getMissionState(@PathVariable Boolean missionState, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (userDetails == null) {throw new CustomException(ErrorCode.MISSION_NOT_FOUND);}
        return missionRepository.findByMissionStateAndUserId(missionState,userDetails.getUser().getId());
    }

    //목표 생성
    @PostMapping("api/category/missions")
    public Mission createMission(@RequestBody MissionRequestDto missionRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return missionService.createMission(missionRequestDto, userDetails);
    }

    //목표 수정
    @PutMapping("/api/category/missions/{missionId}")
    public Long editMission(@RequestBody MissionRequestDto missionRequestDto, @PathVariable Long missionId) {
        System.out.println("미션 수정 시도");
        return missionService.editMission(missionRequestDto, missionId);
    }

    //목표 완료 상태 변경
    @PutMapping("/api/missions/changeMissionState/{missionId}")
    public Long changeMissionState(@PathVariable Long missionId){

        return missionService.changeMissionstate(missionId);
    }

    //목표 삭제
    @DeleteMapping("/api/category/missions/{missionId}")
    public Long deleteMission(@PathVariable Long missionId) {
        System.out.println("미션 삭제 시도");
        return missionService.deleteMission(missionId);
    }

}
