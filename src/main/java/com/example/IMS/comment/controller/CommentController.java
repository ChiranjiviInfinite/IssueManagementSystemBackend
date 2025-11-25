package com.example.IMS.comment.controller;

import com.example.IMS.comment.dto.CommentRequest;
import com.example.IMS.comment.dto.CommentResponse;
import com.example.IMS.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest commentRequest,
            Authentication authentication) {
        CommentResponse response = commentService.addComment(postId, commentRequest, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsByPostId(
            @PathVariable Long postId,
            Authentication authentication) {
        List<CommentResponse> responses = commentService.getCommentsByPostId(postId, authentication.getName());
        return ResponseEntity.ok(responses);
    }
}
