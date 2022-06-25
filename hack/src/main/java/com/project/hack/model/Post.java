package com.project.hack.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String profile_image;

    @Column
    private String likes;

    @Column(nullable = false)
    private String category;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;


    public Post(PostRequestDto requestDto, UserDetailsImpl userDetails) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.name = userDetails.getUser().getName();
        this.email = userDetails.getUser().getEmail();
        this.profile_image = userDetails.getUser().getProfile_img();
        this.category = requestDto.getCategory();
    }

                //게시글 수정
    public void updatePost(PostRequestDto requesteDto) {
        this.title = requesteDto.getTitle();
        this.content = requesteDto.getContent();
        this.category = requesteDto.getCategory();
    }
}
