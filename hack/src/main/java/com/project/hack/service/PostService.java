package com.project.hack.service;

import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.dto.response.PhotoDto;
import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.model.Mission;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import com.project.hack.repository.*;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;

    private final MissionRepository missionRepository;
    private final AwsService awsService;

    public List<PostResponseDto> getPosts() {
        List<PostResponseDto> postList = new ArrayList<>();

        List <Post> findPost = postRepository.findAll();

        for(Post post : findPost) {

            int likes = postLikesRepository.findByPostId(post.getPostId()).size();
            PostResponseDto postResponseDto = new PostResponseDto(post, post.getUser(),likes);
            postList.add(postResponseDto);
        }
        return postList;
    }


    @Transactional
    public Long update(Long postId, PostRequestDto requesteDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다")
        );
        post.updatePost(requesteDto);
        return post.getPostId();
    }

    @Transactional
    public Post createPost(UserDetailsImpl userDetails, PostRequestDto requestDto, List<MultipartFile> multipartFile) {
        PhotoDto photoDtos = awsService.uploadFile(multipartFile);
        Mission mission = missionRepository.findByMissionId(requestDto.getMissionId());
        missionRepository.delete(mission);
        String photoUrl = photoDtos.getPath();
        User user = userDetails.getUser();
        Post post = new Post(requestDto,user,photoUrl);
        return postRepository.save(post);

    }

    public PostResponseDto getpost(Post post) {
        int likes = postLikesRepository.findByPostId(post.getPostId()).size();
        return new PostResponseDto(post,likes);
    }
}
