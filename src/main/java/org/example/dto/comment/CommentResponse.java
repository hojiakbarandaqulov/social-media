package org.example.dto.comment;

import org.example.dto.user.UserSummaryResponse;

import java.time.Instant;

public record CommentResponse(
        Long id,
        String content,
        UserSummaryResponse author,
        Instant createdAt,
        Instant updatedAt
) {
}
