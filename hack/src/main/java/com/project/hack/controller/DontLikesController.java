package com.project.hack.controller;


import com.project.hack.model.User;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.DontLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DontLikesController {
    private final DontLikeService dontLikeService;



    @PostMapping("/api/post/{postId}/dontLike")
    public void postDontLikes(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        dontLikeService.addDontLikes(user,postId);

    }
}
