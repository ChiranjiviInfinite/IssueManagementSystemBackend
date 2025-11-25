package com.example.IMS.post.repository;

import com.example.IMS.post.enums.PostStatus;
import com.example.IMS.post.model.Post;
import com.example.IMS.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByStatus(PostStatus status);

    List<Post> findByCreatedBy(User user);

    List<Post> findByStatusIn(List<PostStatus> statuses);
}
