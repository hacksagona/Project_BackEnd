package com.project.hack.service;

import com.project.hack.dto.request.CommentRequestDto;
import com.project.hack.dto.response.CommentResponseDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
import com.project.hack.model.Comment;
import com.project.hack.model.Post;
import com.project.hack.repository.CommentRepository;
import com.project.hack.repository.PostRepository;
import com.project.hack.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseEntity createComment(CommentRequestDto commentRequestDto, Long postId, UserDetailsImpl userDetails) {
        if(commentRequestDto.getComment().equals("")){
            throw new CustomException(ErrorCode.EMPTY_CONTENT);
        }
        Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("포스트 아이디가 없습니다."));
        Comment comment = new Comment(commentRequestDto, userDetails, post);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return new ResponseEntity(commentResponseDto, HttpStatus.OK);
    }
    
    public ResponseEntity updateComment(CommentRequestDto commentRequestDto, Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentId(commentId);
        if(commentRequestDto.getComment().equals("")){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (userDetails.getUser().getName().equals(comment.getName())){
            comment.updateComment(commentRequestDto,userDetails);
            commentRepository.save(comment);
        }
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return new ResponseEntity(commentResponseDto, HttpStatus.OK);
    }

    public ResponseEntity deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentId(commentId);
        if(userDetails.getUser().getId().equals(comment.getUserId())){
            commentRepository.delete(comment);
            return new ResponseEntity("삭제 완료", HttpStatus.OK);
        }
        throw new CustomException(ErrorCode.INVALID_AUTHORITY);
    }

    public List<Comment> getComment(Long postId) {
        List<Comment> commentList = postRepository.findById(postId).get().getComments();
        return commentList;

    }
}
