package com.project.hack.controller;


import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.model.PostLikes;
import com.project.hack.model.User;
import com.project.hack.repository.PostLikesRepository;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final UserRepository userRepository;
    private final LikeService likeService;
    private final PostLikesRepository postLikesRepository;

    @PostMapping("/api/post/{postId}/like")
    public int addLikes (@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        return likeService.addLikes(user,postId);
    }

    @GetMapping("/api/post/{postId}/likeUser")
    public List<UserResponseDto> getLikeUsers(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return likeService.getLikeUsers(postId,userDetails);
    }


}

