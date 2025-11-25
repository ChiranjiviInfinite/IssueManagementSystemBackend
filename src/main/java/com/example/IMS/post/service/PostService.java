package com.example.IMS.post.service;

import com.example.IMS.post.dto.PostRequest;
import com.example.IMS.post.dto.PostResponse;
import com.example.IMS.post.dto.PostUpdateRequest;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostRequest postRequest, String username);

    PostResponse submitPost(Long postId, String username);

    PostResponse approvePost(Long postId);

    PostResponse rejectPost(Long postId);

    PostResponse closePost(Long postId);

    PostResponse assignUpdate(Long postId, PostUpdateRequest updateRequest);

    PostResponse getPostById(Long postId, String username);

    List<PostResponse> getAllPosts();

    List<PostResponse> getApprovedPosts();

    List<PostResponse> getPostsByUser(String username);
}
