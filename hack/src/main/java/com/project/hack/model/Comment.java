package com.project.hack.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nickname;

    @Column
    private String profile_img;

    @Column (nullable = false)
    private String comment;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails,Post post) {
        this.userId = userDetails.getUser().getId();
        this.comment = commentRequestDto.getComment();
        this.nickname=userDetails.getUser().getNickname();
        this.name = userDetails.getUser().getName();
        this.post = post;
        this.profile_img = userDetails.getUser().getProfile_img();
    }

    public void updateComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        this.nickname = userDetails.getUser().getNickname();
        this.comment = commentRequestDto.getComment();
    }
}
