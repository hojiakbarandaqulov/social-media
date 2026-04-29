package org.example.dto.auth;

import org.example.dto.user.ProfileResponse;

public record AuthResponse(
        String token,
        ProfileResponse user
) {
}
