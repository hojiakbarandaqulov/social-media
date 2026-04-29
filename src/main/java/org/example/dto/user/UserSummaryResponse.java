package org.example.dto.user;

public record UserSummaryResponse(
        Long id,
        String fullName,
        String avatarUrl
) {
}
