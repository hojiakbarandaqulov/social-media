package org.example.mapper;

import org.example.dto.comment.CommentResponse;
import org.example.dto.post.PostResponse;
import org.example.entity.Post;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostMapper {

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

    public PostMapper(UserMapper userMapper, CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.commentMapper = commentMapper;
    }

    public PostResponse toResponse(Post post) {
        List<CommentResponse> comments = post.getComments().stream()
                .map(commentMapper::toResponse)
                .toList();

        return new PostResponse(
                post.getId(),
                post.getContent(),
                userMapper.toSummary(post.getAuthor()),
                comments,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
