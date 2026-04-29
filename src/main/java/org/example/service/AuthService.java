package org.example.service;

import org.example.dto.auth.AuthResponse;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.PasswordUpdateRequest;
import org.example.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    void updatePassword(Long currentUserId, PasswordUpdateRequest request);
}
