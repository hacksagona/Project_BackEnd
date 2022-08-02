package com.project.hack.service;


import com.project.hack.model.PostDontLikes;
import com.project.hack.model.User;
import com.project.hack.repository.PostDontLikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DontLikeService {
    private final PostDontLikesRepository postDontLikesRepository;

    public void addDontLikes(User user, Long postId) {

        if(user ==null){
            throw new IllegalArgumentException("유저가 없음");
        }
        if(postDontLikesRepository.findByUserIdAndPostId(user.getId(),postId).isPresent()){
            PostDontLikes postDontLikes = postDontLikesRepository.findByUserIdAndPostId(user.getId(),postId).orElseThrow(()-> new IllegalArgumentException("해당 좋아요 없음"));
            postDontLikesRepository.delete(postDontLikes);
        }else{
            PostDontLikes postDontLikes = new PostDontLikes(postId,user);
            postDontLikesRepository.save(postDontLikes);
        }
    }

}
