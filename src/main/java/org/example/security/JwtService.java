package org.example.security;

import org.example.entity.User;

public interface JwtService {

    String generateToken(User user);

    String extractUsername(String token);

    boolean isTokenValid(String token, CustomUserPrincipal principal);
}
