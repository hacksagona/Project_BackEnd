package com.project.hack.controller;

import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.model.Post;
import com.project.hack.repository.PostRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;


    // 게시글 작성하기
    @PostMapping("/api/post")
    public Post createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                         @RequestPart(value = "data") PostRequestDto requestDto,
                           @RequestPart(value = "file") List<MultipartFile> multipartFile) {
        return postService.createPost(userDetails,requestDto,multipartFile);

    }
    //포스트 상세페이지
    @GetMapping("/api/post/{postId}")
    public Post getPost(@PathVariable Long postId) {
        Post post = postRepository.findById(postId).orElseThrow (
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return post;
    }


    //게시글 불러오기
    @GetMapping("/api/posts")
    public List<Post> getPosts() {
    return postService.getPosts();
    }

    @PutMapping("/api/post/update/{postId}")
    public Long updatePost(@PathVariable Long postId,
                           @RequestBody PostRequestDto requestDto) {
        return postService.update(postId, requestDto);
    }

    //게시글 삭제

    @DeleteMapping("/api/post/{postId}")
    public Long deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        return postId;
    }
}
