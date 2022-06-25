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
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String profilImg;

    @Column (nullable = false)
    private String content;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails,Post post) {
        this.id = userDetails.getUser().getId();
        this.content = commentRequestDto.getContent();
        this.name = userDetails.getUser().getName();
        this.post = post;
    }

    public void updateComment(CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        this.id = userDetails.getUser().getId();
        this.name = userDetails.getUser().getName();
        this.content = commentRequestDto.getContent();
    }
}
