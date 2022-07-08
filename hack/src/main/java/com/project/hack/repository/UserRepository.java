package com.project.hack.repository;

import com.project.hack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);
    Optional<User> findByGoogleId(Long googleId);
    Optional<User> findByNickname(String nickname);
}
