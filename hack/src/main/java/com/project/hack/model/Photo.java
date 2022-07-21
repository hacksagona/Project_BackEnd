package com.project.hack.model;

import com.project.hack.dto.response.PhotoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;

    @Column(nullable = false)
    private String key1;

    @Column(nullable = false)
    private String url;


    public Photo(PhotoDto photoDto){
        this.key1 = photoDto.getKey();
        this.url = photoDto.getPath();
    }



}