package com.project.hack.controller;

import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import com.project.hack.repository.PostRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;


    @PostMapping("/api/post")
    public Post createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                           @RequestBody PostResponseDto responseDto) {
        User user = userDetails.getUser();
        Long postId = responseDto.getPostId();
        String name = responseDto.getName();
        String title = responseDto.getTitle();
        String email = responseDto.getEmail();
        String content = responseDto.getContent();
        String profile_img = responseDto.getProfile_image();
        String like = responseDto.getLike();
        String category = responseDto.getCategory();
        LocalDateTime modifiedAt = responseDto.getModifiedAt();
        LocalDateTime createdAt = responseDto.getCreatedAt();

        Post post = new Post(postId, title, content, name,
                email, profile_img, like, category, modifiedAt, createdAt);
        return postRepository.save(post);
    }

    //게시글 불러오기
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPosts() {
        return postService.getPosts();
    }

    @PutMapping("/api/post/update/{postId}")
    public Long updatePost(@PathVariable Long postId,
                           @RequestBody PostResponseDto responseDto) {
        return postService.update(postId, responseDto);
    }

    //게시글 삭제
    @DeleteMapping("/api/post/{postId}")
    public Long deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return postId;
    }
}
