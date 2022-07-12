package com.project.hack.service;

import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.dto.response.PhotoDto;
import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.model.Mission;
import com.project.hack.model.Post;
import com.project.hack.model.User;
import com.project.hack.repository.CommentRepository;
import com.project.hack.repository.MissionRepository;
import com.project.hack.repository.PostRepository;
import com.project.hack.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final MissionRepository missionRepository;
    private final AwsService awsService;

    public List<Post> getPosts() {
        List<Post> postList = new ArrayList<Post>();

        List <Post> findPost = postRepository.findAll();

        for(Post post : findPost) {
            Post posts = postRepository.save(post);
            postList.add(posts);
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
        User user = post.getUser();
        return new PostResponseDto(post, user);
    }
}
