package com.project.hack.repository;

import com.project.hack.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    List<Comment> findAllByPostId(Long postId);

    Comment findByCommentId(Long commentId);

}
