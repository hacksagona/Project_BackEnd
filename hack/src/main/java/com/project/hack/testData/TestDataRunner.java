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
        User testUser2 = new User("김준호 Kevin", "김준호님","kevinkim2413@naver.com", passwordEncoder.encode("123456"),profileImg,"kakao");
        User googleUser = new User("Junho Kevin Kim", "구글로그인","junho2413@gmail.com", passwordEncoder.encode("123456"),profileImg,"google");
        userRepository.save(testUser2);
        userRepository.save(googleUser);

        Post post1 = new Post("첫게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/06292782-56a0-41d9-9656-eda47bdcbbda.jpg",googleUser);
        Post post2 = new Post("두번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/08379b04-0b37-4694-a561-29104c2facf8.jpg",googleUser);
        Post post3 = new Post("세번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/26d06be2-3caf-48a8-aec2-faa061cd769c.jpg",testUser2);
        Post post4 = new Post("네번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/1914593d-bfa0-4526-a35a-abe1ef1bd815.png",testUser2);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        int N = 30;
        ArrayList<Post> postArrayList = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            Post postpost = new Post(i+"번째 게시물", "카테고리", "https://hacksagona.s3.ap-northeast-2.amazonaws.com/06292782-56a0-41d9-9656-eda47bdcbbda.jpg",testUser2);
            postRepository.save(postpost);
            postArrayList.add(postpost);
        }

// 테스트 User 의 관심상품 등록
// 검색어 당 관심상품 10개 등록
//        createTestData(testUser, "신발");
//        createTestData(testUser, "과자");
//        createTestData(testUser, "키보드");
//        createTestData(testUser, "휴지");
//        createTestData(testUser, "휴대폰");
//        createTestData(testUser, "앨범");
//        createTestData(testUser, "헤드폰");
//        createTestData(testUser, "이어폰");
//        createTestData(testUser, "노트북");
//        createTestData(testUser, "무선 이어폰");
//        createTestData(testUser, "모니터");
    }

//    private void createTestData(User user, String searchWord) throws IOException {
//// 네이버 쇼핑 API 통해 상품 검색
//        List<ItemDto> itemDtoList = itemSearchService.getItems(searchWord);
//
//        List<Product> productList = new ArrayList<>();
//
//        for (ItemDto itemDto : itemDtoList) {
//            Product product = new Product();
//// 관심상품 저장 사용자
//            product.setUserId(user.getId());
//// 관심상품 정보
//            product.setTitle(itemDto.getTitle());
//            product.setLink(itemDto.getLink());
//            product.setImage(itemDto.getImage());
//            product.setLprice(itemDto.getLprice());
//
//// 희망 최저가 랜덤값 생성
//// 최저 (100원) ~ 최대 (상품의 현재 최저가 + 10000원)
//            int myPrice = getRandomNumber(MIN_MY_PRICE, itemDto.getLprice() + 10000);
//            product.setMyprice(myPrice);
//
//            productList.add(product);
//        }
//
//        productRepository.saveAll(productList);
//    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}