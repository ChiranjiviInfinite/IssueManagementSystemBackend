package com.example.IMS.post.dto;

import com.example.IMS.post.enums.PostStatus;
import com.example.IMS.post.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String description;
    private PostType type;
    private PostStatus status;
    private String createdByUsername;
    private String assignedUpdate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
