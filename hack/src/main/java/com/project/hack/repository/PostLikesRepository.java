package com.project.hack.repository;

import com.project.hack.model.Post;
import com.project.hack.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikesRepository extends JpaRepository<PostLikes,Long> {

    Optional<PostLikes> findByUserIdAndPost(Long userId, Post post);
    List<PostLikes> findByPost(Post post);
    List<PostLikes> findByUserId(Long id);
}
