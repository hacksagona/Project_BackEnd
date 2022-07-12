package com.project.hack.service;

import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.model.Post;
import com.project.hack.model.PostLikes;
import com.project.hack.model.User;
import com.project.hack.repository.PostLikesRepository;
import com.project.hack.repository.UserRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LikeService {

    private final PostLikesRepository postLikesRepository;
    private final UserRepository userRepository;
    public int addLikes(User user, Long postId) {

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

    public List<UserResponseDto> getLikeUsers(Long postId, UserDetailsImpl userDetails) {
        List<PostLikes> postLikesList = postLikesRepository.findByPostId(postId);
        List<UserResponseDto> users = new ArrayList<>();
        for(PostLikes postLikes : postLikesList){
            User user = userRepository.findById(postLikes.getUserId()).orElseThrow(()->new IllegalArgumentException("해당 유저가 없습니다"));
            UserResponseDto responseDto = new UserResponseDto().builder()
                    .userId(user.getId())
                    .email(user.getEmail())
                    .profile_img(user.getProfile_img())
                    .nickname(user.getNickname())
                    .build();
            users.add(responseDto);
        }

        return users;


    }
}
