package com.project.hack.testData;


import com.project.hack.model.Post;
import com.project.hack.model.User;
import com.project.hack.repository.PostRepository;
import com.project.hack.repository.UserRepository;
import com.project.hack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;

@RequiredArgsConstructor
@Component
public class TestDataRunner implements ApplicationRunner {

    @Value("${cloud.aws.s3.profileimg}")
    private String profileImg;

    private final UserService userService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
// 테스트 User 생성
        User testUser2 = userRepository.findById(1L).orElse(null);
        User googleUser = userRepository.findById(2L).orElse(null);


//        Post post1 = new Post("첫게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/06292782-56a0-41d9-9656-eda47bdcbbda.jpg",googleUser);
//        Post post2 = new Post("두번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/08379b04-0b37-4694-a561-29104c2facf8.jpg",googleUser);
//        Post post3 = new Post("세번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/26d06be2-3caf-48a8-aec2-faa061cd769c.jpg",testUser2);
//        Post post4 = new Post("네번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/1914593d-bfa0-4526-a35a-abe1ef1bd815.png",testUser2);
//        postRepository.save(post1);
//        postRepository.save(post2);
//        postRepository.save(post3);
//        postRepository.save(post4);
//
//        int N = 30;
//        ArrayList<Post> postArrayList = new ArrayList<>();
//        for (int i = 0; i < N; i++) {
//            Post postpost = new Post(i+"번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/06292782-56a0-41d9-9656-eda47bdcbbda.jpg",testUser2);
//            postRepository.save(postpost);
//            postArrayList.add(postpost);
//        }

    }
}