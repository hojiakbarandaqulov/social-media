package org.example.repository;

import org.example.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"author", "comments", "comments.author"})
    List<Post> findAllByOrderByCreatedAtDesc();

    @Override
    @EntityGraph(attributePaths = {"author", "comments", "comments.author"})
    Optional<Post> findById(Long id);
}
