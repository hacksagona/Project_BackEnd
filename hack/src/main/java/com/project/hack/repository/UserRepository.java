package com.project.hack.repository;

import com.project.hack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    @Query(nativeQuery = true, value = "select * from user ur where ur.social =:social and ur.email =:email")
    Optional<User> findByEmailAndSocial(String email,String social);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
