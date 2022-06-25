package com.project.hack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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

    @Column(nullable = false)
    private String like;

    @Column(nullable = false)
    private String category;

    @JsonIgnore //필수
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();


    public Post(UserDetailsImpl userDetails, PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.name = userDetails.getUser().getName();
        this.email = userDetails.getUser().getEmail();
//      this.profile_image = userDetials.getUser().getProfile_img();
        this.category = requestDto.getCategory();
    }


//    // 코멘트 클래스와 one-to-many 팹핑
//    @JsonManagedReference
//    @OneToMany(mappedBy = "post", orphanRemoval = true)
//    private List<Comment> comments;

                //게시글 수정
    public void updatePost(PostRequestDto requesteDto) {
        this.title = requesteDto.getTitle();
        this.content = requesteDto.getContent();
        this.category = requesteDto.getCategory();
    }



    //{
    //”postId” :
    //”title” :
    //”content” :
    //”name” :
    //”email” :
    //”profile_img” :
    //”like”(좋아요):
    //”category”(카테고리) :
    //”modifiedAt” :
    //”createdAt”:
    //}







}
