package com.project.hack.model;

import com.project.hack.dto.request.CommentRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long postId;

    @Column(nullable = false)
    private String name;

    @Column
    private String profilImg;

    @Column (nullable = false)
    private String content;


    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;


    public Comment(Long commentId, Long postId, String name, String profilImg, String content, Post post) {
        this.commentId = commentId;
        this.postId = postId;
        this.name = name;
        this.profilImg = profilImg;
        this.content = content;
        this.post = post;
    }

    public void updateComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        this.id = userDetails.getUser().getId();
        this.name = userDetails.getUser().getNickname();
        this.content = commentRequestDto.getContent();
    }
}
