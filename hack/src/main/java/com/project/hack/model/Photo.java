package com.project.hack.model;

import com.project.hack.dto.response.PhotoDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String key;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name ="postId")
    private Post post;

    public Photo(PhotoDto photoDto){
        this.key = photoDto.getKey();
        this.url = photoDto.getPath();
    }



}