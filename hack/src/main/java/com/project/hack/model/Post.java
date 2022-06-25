package com.project.hack.model;

import com.project.hack.dto.response.PostResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

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



    public Post(Long postId, String title, String content,
                String name, String email, String profile_img,
                String like, String category, LocalDateTime modifiedAt,
                LocalDateTime createdAt) {
        super();
    }


//    // 코멘트 클래스와 one-to-many 팹핑
//    @JsonManagedReference
//    @OneToMany(mappedBy = "post", orphanRemoval = true)
//    private List<Comment> comments;

    //게시글 수정
    public void updatePost(PostResponseDto responseDto) {
        this.title = responseDto.getTitle();
        this.content = responseDto.getContent();
        this.category = responseDto.getCategory();
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
