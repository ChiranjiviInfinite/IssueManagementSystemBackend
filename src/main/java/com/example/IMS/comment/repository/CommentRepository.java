package com.example.IMS.comment.repository;

import com.example.IMS.comment.model.Comment;
import com.example.IMS.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);

    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}