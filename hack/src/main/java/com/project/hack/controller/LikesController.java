package com.project.hack.controller;


import com.project.hack.model.PostLikes;
import com.project.hack.model.User;
import com.project.hack.repository.PostLikesRepository;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LikesController {
    private final UserRepository userRepository;
    private final PostLikesRepository postLikesRepository;

    @PostMapping("/api/post/{postId}/likes")
    public int addLikes (@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        User user = userDetails.getUser();
        if(user ==null){
            throw new IllegalArgumentException("유저가 없음");
        }
        if(postLikesRepository.findByUserIdAndPostId(user.getId(),postId).isPresent()){
            PostLikes postLikes = postLikesRepository.findByUserIdAndPostId(user.getId(),postId).orElseThrow(()-> new IllegalArgumentException("해당 좋아요 없음"));
            postLikesRepository.delete(postLikes);
        }else{
            PostLikes postLikes = new PostLikes(postId,user);
            postLikesRepository.save(postLikes);
        }
        int count = 0;

        List<PostLikes> postLikesList= postLikesRepository.findByPostId(postId);
        count = postLikesList.size();

        return count;
    }


}

