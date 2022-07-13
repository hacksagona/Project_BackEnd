package com.project.hack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long postId;

    @Column(nullable = false)
    private String postContent;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String photoUrl;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;


    public Post(PostRequestDto requestDto, User user, String photoUrl) {
        this.postContent = requestDto.getPostContent();
        this.category = requestDto.getCategory();
        this.photoUrl = photoUrl;
        this.user = user;
    }

    //테스터 데이터를 위한 생성자
    public Post(String postContent, String category, String photoUrl, User user) {
        this.postContent = postContent;
        this.category = category;
        this.photoUrl = photoUrl;
        this.user = user;
    }

    //게시글 수정
    public void updatePost(PostRequestDto requesteDto) {

    }
}
