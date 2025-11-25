package com.example.IMS.post.controller;

import com.example.IMS.post.dto.PostRequest;
import com.example.IMS.post.dto.PostResponse;
import com.example.IMS.post.dto.PostUpdateRequest;
import com.example.IMS.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Valid @RequestBody PostRequest postRequest,
            Authentication authentication) {
        PostResponse response = postService.createPost(postRequest, authentication.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/submit")
    public ResponseEntity<PostResponse> submitPost(
            @PathVariable Long id,
            Authentication authentication) {
        PostResponse response = postService.submitPost(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> approvePost(@PathVariable Long id) {
        PostResponse response = postService.approvePost(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> rejectPost(@PathVariable Long id) {
        PostResponse response = postService.rejectPost(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/close")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> closePost(@PathVariable Long id) {
        PostResponse response = postService.closePost(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/assign-update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PostResponse> assignUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequest updateRequest) {
        PostResponse response = postService.assignUpdate(id, updateRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(
            @PathVariable Long id,
            Authentication authentication) {
        PostResponse response = postService.getPostById(id, authentication.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostResponse> responses = postService.getAllPosts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/approved")
    public ResponseEntity<List<PostResponse>> getApprovedPosts() {
        List<PostResponse> responses = postService.getApprovedPosts();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/user/posts")
    public ResponseEntity<List<PostResponse>> getUserPosts(Authentication authentication) {
        List<PostResponse> responses = postService.getPostsByUser(authentication.getName());
        return ResponseEntity.ok(responses);
    }
}
