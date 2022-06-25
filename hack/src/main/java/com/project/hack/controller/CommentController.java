package com.project.hack.controller;

import com.project.hack.dto.request.CommentRequestDto;
import com.project.hack.exception.CustomException;
import com.project.hack.exception.ErrorCode;
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

    @PostMapping("/api/post/{postId}/comment")
    public ResponseEntity createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}
        return commentService.createComment(requestDto, postId, userDetails);
    }

    @PutMapping("/api/post/{postId}/comment/{commentId}")
    public ResponseEntity updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}
        return commentService.updateComment(requestDto, commentId, userDetails);
    }

    @DeleteMapping("/api/post/{postId}/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        if(userDetails == null) {throw new CustomException(ErrorCode.AUTH_TOKEN_NOT_FOUND);}
        return commentService.deleteComment(commentId, userDetails);
    }
}

