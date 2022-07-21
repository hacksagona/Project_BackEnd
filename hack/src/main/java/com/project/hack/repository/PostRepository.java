package com.project.hack.repository;

import com.project.hack.model.Post;
import com.project.hack.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    Page<Post> findAll(Pageable pageable);
}
