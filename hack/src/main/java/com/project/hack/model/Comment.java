package com.project.hack.model;

import com.project.hack.dto.request.CommentRequestDto;
import com.project.hack.security.UserDetailsImpl;
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
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private Long id;


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


    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        this.id = userDetails.getUser().getId();
        this.content = commentRequestDto.getContent();
        this.postId = getPostId();
        this.name = userDetails.getUser().getName();
    }

    public void updateComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        this.id = userDetails.getUser().getId();
        this.name = userDetails.getUser().getName();
        this.content = commentRequestDto.getContent();
    }
}
