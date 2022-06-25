package com.project.hack.service;

import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.model.Post;
import com.project.hack.repository.PostRepository;
import com.project.hack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public List<PostResponseDto> getPosts() {
        List<PostResponseDto> responseList = new ArrayList<>();
        List<Post> posts = postRepository.findAll();

        for(Post post : posts) {

            PostResponseDto postResponseDto = new PostResponseDto(post);
            responseList.add(postResponseDto);

        }
        return responseList;
    }

    @Transactional
    public Long update(Long postId, PostResponseDto responseDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
        );
        post.updatePost(responseDto);
        return post.getPostId();
    }
}
