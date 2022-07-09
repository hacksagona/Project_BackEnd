package com.project.hack.model;

import com.project.hack.dto.request.MissionRequestDto;
import com.project.hack.enums.Category;
import com.project.hack.security.UserDetailsImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.thymeleaf.util.StringUtils;

import javax.persistence.*;
//import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Mission /*implements AttributeConverter<Category, String>*/{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long missionId;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String missionContent;

    @Column(nullable = false)
    private Boolean missionState;

//    @Convert(converter = XXX)
//    @Column(name = "mission_category")
//    private String category;


    public Mission(MissionRequestDto missionRequestDto, UserDetailsImpl userDetails) {
        this.missionContent = missionRequestDto.getMissionContent();
        this.category = missionRequestDto.getCategory();
        this.userId = userDetails.getUser().getId();
        this.missionState = false;
    }

    public void fixMission(MissionRequestDto missionRequestDto) {
        this.missionContent = missionRequestDto.getMissionContent();
    }

    public void changeMissionState(boolean missionState){
        this.missionState = missionState;
    }

//    @Override
//    public String convertToDatabaseColumn(Category attribute) {
//        if (Objects.isNull(attribute)) {
//            return null;
//        }
//        return attribute.getValue();
//    }
//
//    @Override
//    public Category convertToEntityAttribute(String dbData) {
//        if (Str)
//        return null;
//    }
}

