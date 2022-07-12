package com.project.hack.dto.response;

import com.project.hack.model.Comment;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long postId;

    private String postContent;


    private String nickname;
    private String photoUrl;


    private String email;


    private String profile_img;


    private int likes;

    private String category;

    private List<Comment> commentList;

    public PostResponseDto(Post post, int likes) {
        this.postId = post.getPostId();
        this.postContent = post.getPostContent();
        this.nickname = post.getUser().getNickname();
        this.email = post.getUser().getEmail();
        this.profile_img = post.getUser().getProfile_img();
        this.category = post.getCategory();
        this.modifiedAt = post.getModifiedAt();
        this.createdAt = post.getCreatedAt();
        this.photoUrl = post.getPhotoUrl();
        this.likes = likes;
        this.commentList = post.getComments();
    }
    public PostResponseDto(Post post, User user, int likes) {
        this.postId = post.getPostId();
        this.postContent = post.getPostContent();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profile_img = user.getProfile_img();
        this.category = post.getCategory();
        this.modifiedAt = post.getModifiedAt();
        this.createdAt = post.getCreatedAt();
        this.photoUrl = post.getPhotoUrl();
        this.likes = likes;
    }





}
