package com.project.hack.controller;

import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import com.project.hack.repository.PostLikesRepository;
import com.project.hack.repository.PostRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;


    // 게시글 작성하기
    @PostMapping("/api/post")
    public Post createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                         @RequestPart(value = "data") PostRequestDto requestDto,
                           @RequestPart(value = "file") List<MultipartFile> multipartFile) {
        return postService.createPost(userDetails,requestDto,multipartFile);

    }
    //포스트 상세페이지
    @GetMapping("/api/post/{postId}")
    public PostResponseDto getPost(@PathVariable Long postId) {
        Post post = postRepository.findById(postId).orElseThrow (
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return postService.getpost(post);
    }


    //게시글 불러오기
    @GetMapping("/api/posts")
    public List<PostResponseDto> getPosts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userDetails.getUser();
    return postService.getPosts(page, size,sortBy, isAsc,user);
    }

    @GetMapping("/api/posts/mypost")
    public List<PostResponseDto> getMyposts(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return postService.getMyPosts(userDetails);}

    @PutMapping("/api/post/update/{postId}")
    public Long updatePost(@PathVariable Long postId,
                           @RequestBody PostRequestDto requestDto) {
        return postService.update(postId, requestDto);
    }

    //게시글 삭제

    @DeleteMapping("/api/post/{postId}")
    public Long deletePost(@PathVariable Long postId) {
        postRepository.deleteById(postId);
        System.out.println("포스트 삭제");
        return postId;
    }
    // =============== 골샷 ====================
    @GetMapping("/api/posts/goalShot")
    public List<PostResponseDto> getGoalShotPost(@AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println("골샷 조회");
        User user = userDetails.getUser();
        return postService.getGoalShotPost(user);
    }

    @GetMapping("/api/posts/todayLikes")
    public List<PostResponseDto> getTodayLikes(@AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println("오늘 좋아요 누른 게시물 조회");
        User user = userDetails.getUser();
        return postService.getTodayLikes(user);
    }
}
