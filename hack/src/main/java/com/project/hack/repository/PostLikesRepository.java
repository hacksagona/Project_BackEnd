package com.project.hack.repository;

import com.project.hack.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes,Long> {

    Optional<PostLikes> findByUserIdAndPostId(Long userId, Long postId);
    List<PostLikes> findByPostId(Long postId);

    List<PostLikes> findByUserId(Long id);
}
