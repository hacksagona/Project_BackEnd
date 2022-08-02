package com.project.hack.service;

import com.project.hack.dto.response.UserResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Post;
import com.project.hack.model.PostLikes;
import com.project.hack.model.User;
import com.project.hack.repository.PostLikesRepository;
import com.project.hack.repository.PostRepository;
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
    private final PostRepository postRepository;
    public int addLikes(User user, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));

        if(user ==null){
            throw new IllegalArgumentException("유저가 없음");
        }
        if(postLikesRepository.findByUserIdAndPost(user.getId(),post).isPresent()){
            PostLikes postLikes = postLikesRepository.findByUserIdAndPost(user.getId(),post).orElseThrow(()-> new IllegalArgumentException("해당 좋아요 없음"));
            postLikesRepository.delete(postLikes);
        }else{
            PostLikes postLikes = new PostLikes(post,user);
            postLikesRepository.save(postLikes);
        }
        int count = 0;

        List<PostLikes> postLikesList= postLikesRepository.findByPost(post);
        count = postLikesList.size();

        return count;
    }

    public List<UserResponseDto> getLikeUsers(Long postId, UserDetailsImpl userDetails) {
        System.out.println("좋아요 누른사람들 정보 얻기 api 작동");
        Post post = postRepository.findById(postId).orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
        List<PostLikes> postLikesList = postLikesRepository.findByPost(post);
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

    public boolean getIsLiked(Long postId, UserDetailsImpl userDetails) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
        return postLikesRepository.findByUserIdAndPost(userDetails.getUser().getId(),post).isPresent();

    }
}
