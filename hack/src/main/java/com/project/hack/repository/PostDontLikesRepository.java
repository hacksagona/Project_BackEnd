package com.project.hack.repository;

import com.project.hack.model.PostDontLikes;
import com.project.hack.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostDontLikesRepository extends JpaRepository<PostDontLikes, Long> {

    Optional<PostDontLikes> findByUserIdAndPostId(Long userId, Long postId);
    List<PostDontLikes> findByPostId(Long postId);
}
