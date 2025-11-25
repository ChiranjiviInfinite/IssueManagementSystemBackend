package com.example.IMS.comment.service.impl;

import com.example.IMS.comment.dto.CommentRequest;
import com.example.IMS.comment.dto.CommentResponse;
import com.example.IMS.comment.model.Comment;
import com.example.IMS.comment.repository.CommentRepository;
import com.example.IMS.comment.service.CommentService;
import com.example.IMS.exception.ResourceNotFoundException;
import com.example.IMS.exception.UnauthorizedException;
import com.example.IMS.post.enums.PostStatus;
import com.example.IMS.post.model.Post;
import com.example.IMS.post.repository.PostRepository;
import com.example.IMS.user.enums.RoleType;
import com.example.IMS.user.model.User;
import com.example.IMS.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Override
    @Transactional
    public CommentResponse addComment(Long postId, CommentRequest commentRequest, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userService.findByUsername(username);

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ADMIN);

        //boolean isOwner = post.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && post.getStatus() != PostStatus.APPROVED) {
            throw new UnauthorizedException("You can only comment on approved posts");
        }

        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setPost(post);
        comment.setCreatedBy(user);

        Comment savedComment = commentRepository.save(comment);
        return convertToResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        User user = userService.findByUsername(username);

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName() == RoleType.ADMIN);

        boolean isOwner = post.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isOwner && post.getStatus() != PostStatus.APPROVED) {
            throw new UnauthorizedException("You don't have permission to view comments on this post");
        }

        return commentRepository.findByPostOrderByCreatedAtDesc(post).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse convertToResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setText(comment.getText());
        response.setPostId(comment.getPost().getId());
        response.setCreatedByUsername(comment.getCreatedBy().getUsername());
        response.setCreatedAt(comment.getCreatedAt());
        return response;
    }
}