package com.project.hack.controller;

import com.project.hack.dto.request.CommentRequestDto;
import com.project.hack.exceptionHandler.CustomException;
import com.project.hack.exceptionHandler.ErrorCode;
import com.project.hack.model.Post;
import com.project.hack.security.UserDetailsImpl;
import com.project.hack.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/api/post/{post-id}/comment")
    public ResponseEntity createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails, Post post){
        if(userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}
        return commentService.createComment(requestDto, postid, userDetails, post);
    }

    @PutMapping("/api/post/{post-id}/comment/{comment-id}")
    public ResponseEntity updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}
        return commentService.updateComment(requestDto, commentId, userDetails);
    }

    @DeleteMapping("/api/post/{post-id}/comment/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}
        return commentService.deleteComment(commentId, userDetails);
    }
}

