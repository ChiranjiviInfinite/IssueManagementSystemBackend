package com.example.IMS.comment.service;


import com.example.IMS.comment.dto.CommentRequest;
import com.example.IMS.comment.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse addComment(Long postId, CommentRequest commentRequest, String username);

    List<CommentResponse> getCommentsByPostId(Long postId, String username);
}