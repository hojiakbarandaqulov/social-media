package org.example.dto.admin;

import org.example.enums.Role;
import org.example.enums.UserStatus;

import java.time.Instant;

public record AdminUserResponse(
        Long id,
        String fullName,
        String email,
        String avatarUrl,
        String bio,
        Role role,
        UserStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
