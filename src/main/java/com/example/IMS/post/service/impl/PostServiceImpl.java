package com.example.IMS.post.service.impl;

import com.example.IMS.exception.InvalidOperationException;
import com.example.IMS.exception.ResourceNotFoundException;
import com.example.IMS.exception.UnauthorizedException;
import com.example.IMS.post.dto.PostRequest;
import com.example.IMS.post.dto.PostResponse;
import com.example.IMS.post.dto.PostUpdateRequest;
import com.example.IMS.post.enums.PostStatus;
import com.example.IMS.post.model.Post;
import com.example.IMS.post.repository.PostRepository;
import com.example.IMS.post.service.PostService;
import com.example.IMS.user.enums.RoleType;
import com.example.IMS.user.model.User;
import com.example.IMS.user.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    @Transactional
    public PostResponse createPost(PostRequest postRequest, String username) {
        User user = userService.findByUsername(username);

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setType(postRequest.getType());
        post.setStatus(PostStatus.DRAFT);
        post.setCreatedBy(user);

        Post savedPost = postRepository.save(post);
        return convertToResponse(savedPost);
    }

    @Override
    @Transactional
    public PostResponse submitPost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userService.findByUsername(username);

        if (!post.getCreatedBy().getId().equals(user.getId())) {
            throw new UnauthorizedException("You can only submit your own posts");
        }

        if (post.getStatus() != PostStatus.DRAFT) {
            throw new InvalidOperationException("Only DRAFT posts can be submitted for approval");
        }

        post.setStatus(PostStatus.PENDING_APPROVAL);
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost);
    }

    @Override
    @Transactional
    public PostResponse approvePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (post.getStatus() != PostStatus.PENDING_APPROVAL) {
            throw new InvalidOperationException("Only PENDING_APPROVAL posts can be approved");
        }

        post.setStatus(PostStatus.APPROVED);
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost);
    }

    @Override
    @Transactional
    public PostResponse rejectPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (post.getStatus() != PostStatus.PENDING_APPROVAL) {
            throw new InvalidOperationException("Only PENDING_APPROVAL posts can be rejected");
        }

        post.setStatus(PostStatus.REJECTED);
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost);
    }

    @Override
    @Transactional
    public PostResponse closePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        if (post.getStatus() != PostStatus.APPROVED) {
            throw new InvalidOperationException("Only APPROVED posts can be closed");
        }

        post.setStatus(PostStatus.CLOSED);
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost);
    }

    @Override
    @Transactional
    public PostResponse assignUpdate(Long postId, PostUpdateRequest updateRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        post.setAssignedUpdate(updateRequest.getAssignedUpdate());
        Post updatedPost = postRepository.save(post);
        return convertToResponse(updatedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userService.findByUsername(username);

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ADMIN);

        boolean isOwner = post.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner && post.getStatus() != PostStatus.APPROVED) {
            throw new UnauthorizedException("You don't have permission to view this post");
        }

        return convertToResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getApprovedPosts() {
        return postRepository.findByStatus(PostStatus.APPROVED).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUser(String username) {
        User user = userService.findByUsername(username);
        return postRepository.findByCreatedBy(user).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private PostResponse convertToResponse(Post post) {
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setDescription(post.getDescription());
        response.setType(post.getType());
        response.setStatus(post.getStatus());
        response.setCreatedByUsername(post.getCreatedBy().getUsername());
        response.setAssignedUpdate(post.getAssignedUpdate());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());
        return response;
    }
}
