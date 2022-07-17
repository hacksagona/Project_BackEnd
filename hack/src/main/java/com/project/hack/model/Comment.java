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

    @Column (nullable = false)
    private String comment;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "User_ID")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails,Post post) {
        this.comment = commentRequestDto.getComment();
        this.post = post;
        this.user = userDetails.getUser();
    }

    public void updateComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        this.user = userDetails.getUser();
        this.comment = commentRequestDto.getComment();
    }
}
