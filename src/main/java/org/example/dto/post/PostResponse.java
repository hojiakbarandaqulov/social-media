package org.example.dto.post;

import org.example.dto.comment.CommentResponse;
import org.example.dto.user.UserSummaryResponse;

import java.time.Instant;
import java.util.List;

public record PostResponse(
        Long id,
        String content,
        UserSummaryResponse author,
        List<CommentResponse> comments,
        Instant createdAt,
        Instant updatedAt
) {
}
