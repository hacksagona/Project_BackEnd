package com.project.hack.repository;

import com.project.hack.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long>{

    Mission findByMissionId(Long missionId);
    List<Mission> findByMissionState(Boolean missionState);
    Mission findByCategory(String category);
}
