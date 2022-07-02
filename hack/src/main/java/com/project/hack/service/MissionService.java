package com.project.hack.service;

import com.project.hack.dto.response.MissionResponseDto;
import com.project.hack.model.Mission;
import com.project.hack.repository.MissionRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MissionService {

    private final MissionRepository missionRepository;

    public List<Mission> getMissions(MissionResponseDto missionResponseDto, UserDetailsImpl userDetails, Long missionId) {


        return
    }
}
