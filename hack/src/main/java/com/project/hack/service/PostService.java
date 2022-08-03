package com.project.hack.service;

import com.project.hack.dto.request.PostRequestDto;
import com.project.hack.dto.response.PhotoDto;
import com.project.hack.dto.response.PostPageResponseDto;
import com.project.hack.dto.response.PostResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Mission;
import com.project.hack.model.Post;
import com.project.hack.model.PostLikes;
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

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikesRepository postLikesRepository;
    private final PostDontLikesRepository postDontLikesRepository;

    private final MissionRepository missionRepository;
    private final AwsService awsService;

    public PostPageResponseDto getPosts(int page, int size, String sortBy, boolean isAsc, User user) {

        System.out.println(page + "");
        Sort.Direction direction = isAsc? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Post> findPost = postRepository.findAll(pageable);
        List<PostResponseDto> postList = new ArrayList<>();

        for (Post post : findPost) {
            int likes = postLikesRepository.findByPost(post).size();
            PostResponseDto postResponseDto = new PostResponseDto(post, post.getUser(), likes);
            postList.add(postResponseDto);
        }
        return new PostPageResponseDto(findPost.isLast(), findPost.getTotalPages() ,postList);
    }

    public List<PostResponseDto> getMyPosts(UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        List<PostResponseDto> postList = new ArrayList<>();

        List <Post> findPost = postRepository.findByUser(user);

        for(Post post : findPost) {

            int likes = postLikesRepository.findByPost(post).size();
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
        int likes = postLikesRepository.findByPost(post).size();
        return new PostResponseDto(post,likes);
    }

    public List<PostResponseDto> getGoalShotPost(User user) {

        List<Post> posts = postRepository.findAll();
//        List<Mission> missionList = missionRepository.findByUserId(user.getId());
//        List<Post> postList = postRepository.findByUser(user);
//        for(Post post : postList){
//            if(post.getCategory()==""
//        }

        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        Collections.shuffle(posts);
        int i = 0;
        for(Post post : posts){
            if(i==11){i =0; break;}
            Long postId = post.getPostId();
            Long userId = user.getId();
            if(!postDontLikesRepository.findByUserIdAndPostId(userId, postId).isPresent()
                    &&!postLikesRepository.findByUserIdAndPost(userId,post).isPresent()
                    && !Objects.equals(post.getUser().getId(), user.getId())){
                int likes = postLikesRepository.findByPost(post).size();
                PostResponseDto postResponseDto = new PostResponseDto(post, post.getUser(),likes);
                postResponseDtos.add(postResponseDto);
                i++;
            }
        }
        return postResponseDtos;
    }

    public List<PostResponseDto> getTodayLikes(User user) {
        List<PostResponseDto> postResponseDtos =new ArrayList<>();
        List<PostLikes> postLikesList = postLikesRepository.findByUserId(user.getId());
        LocalDate now = LocalDate.now();
        int nowDay = now.getDayOfMonth();
        if(postLikesList.size() ==0){return postResponseDtos;}
        for (PostLikes postLikes : postLikesList){
            int likeDay = postLikes.getCreatedAtDateOnly().getDayOfMonth();
            if(nowDay == likeDay){
                Post post = postRepository.findById(postLikes.getPost().getPostId()).orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
                if(!Objects.equals(post.getUser().getId(), user.getId())){
                    int likes = postLikesRepository.findByPost(post).size();
                    PostResponseDto postResponseDto = new PostResponseDto(post, post.getUser(),likes);
                    postResponseDtos.add(postResponseDto);
                }

            }
        }
        return postResponseDtos;
    }
}
