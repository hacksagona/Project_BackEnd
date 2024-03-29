package com.project.hack.dto.response;

import com.project.hack.model.Comment;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Long postId;

    private String postContent;

    private Long userId;


    private String nickname;
    private String photoUrl;


    private String email;


    private String profile_img;


    private int likes;

    private String category;

    private List<Comment> commentList;

    public PostResponseDto(Post post, int likes) {
        this.userId = post.getUser().getId();
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
        this.userId = user.getId();
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
