package org.example.mapper;

import org.example.dto.comment.CommentResponse;
import org.example.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                userMapper.toSummary(comment.getAuthor()),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
