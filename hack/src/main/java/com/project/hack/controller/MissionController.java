package com.project.hack.controller;

import com.project.hack.model.Mission;
import com.project.hack.repository.MissionRepository;
import com.project.hack.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<Mission> getMissions() {

        return MissionService.getMissions();
    }
}
