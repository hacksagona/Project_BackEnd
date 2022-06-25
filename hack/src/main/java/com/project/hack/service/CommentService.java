package com.project.hack.service;

import com.project.hack.dto.request.CommentRequestDto;
import com.project.hack.dto.response.CommentResponseDto;
import com.project.hack.exceptionHandler.CustomException;
import com.project.hack.exceptionHandler.ErrorCode;
import com.project.hack.model.Comment;
import com.project.hack.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;


    public ResponseEntity createComment(CommentRequestDto commentRequestDto, Long commentId, UserDetailsImpl userDetails, Post post) {
        if(commentRequestDto.getContent().equals("")){
            throw new CustomException(ErrorCode.EMPTY_CONTENT);
        }
        Comment comment = new Comment(commentRequestDto, postId, userDetails, post);
        commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return new ResponseEntity(commentResponseDto, HttpStatus.OK);
    }

    public ResponseEntity updateComment(CommentRequestDto commentRequestDto, Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentId(commentId);
        if(commentRequestDto.getContent().equals("")){
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        if (userDetails.getUser().getname().equals(comment.getname())){
            comment.updateComment(commentRequestDto,userDetails);
            commentRepository.save(comment);
        }
        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return new ResponseEntity(commentResponseDto, HttpStatus.OK);
    }

    public ResponseEntity deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findByCommentId(commentId);
        if(userDetails.getUser().getId().equals(comment.getCommentId())){
            commentRepository.delete(comment);
            return new ResponseEntity("삭제 완료", HttpStatus.OK);
        }
        throw new CustomException(ErrorCode.INVALID_AUTHORITY);
    }
}