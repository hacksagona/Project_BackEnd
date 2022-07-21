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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final PostDontLikesRepository postDontLikesRepository;

    private final MissionRepository missionRepository;
    private final AwsService awsService;

    public List<PostResponseDto> getPosts(int page, int size, String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> findPost = postRepository.findAll(pageable);
        List<PostResponseDto> postList = new ArrayList<>();

        for(Post post : findPost) {

            int likes = postLikesRepository.findByPostId(post.getPostId()).size();
            PostResponseDto postResponseDto = new PostResponseDto(post, post.getUser(),likes);
            postList.add(postResponseDto);
        }
        return postList;
    }

    public List<PostResponseDto> getMyPosts(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        List<PostResponseDto> postList = new ArrayList<>();

        List <Post> findPost = postRepository.findByUser(user);

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

    public List<PostResponseDto> getGoalShotPost(User user) {

        List<Post> posts = postRepository.findAll();
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for(Post post : posts){
            Long postId = post.getPostId();

            Long userId = user.getId();
            if(!postDontLikesRepository.findByUserIdAndPostId(userId, postId).isPresent() &&!postLikesRepository.findByUserIdAndPostId(userId,postId).isPresent()){
                int likes = postLikesRepository.findByPostId(post.getPostId()).size();
                PostResponseDto postResponseDto = new PostResponseDto(post, post.getUser(),likes);
                postResponseDtos.add(postResponseDto);
            }
        }
        return postResponseDtos;
    }


}
