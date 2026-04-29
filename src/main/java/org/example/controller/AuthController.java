package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.api.ApiMessageResponse;
import org.example.dto.auth.AuthResponse;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.PasswordUpdateRequest;
import org.example.dto.auth.RegisterRequest;
import org.example.security.CustomUserPrincipal;
import org.example.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Auth", description = "Registratsiya, login va password amallari")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Yangi foydalanuvchini ro'yxatdan o'tkazish")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @Operation(summary = "Foydalanuvchi login qilishi")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(
            summary = "Joriy foydalanuvchi parolini yangilash",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/password")
    public ResponseEntity<ApiMessageResponse> updatePassword(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody PasswordUpdateRequest request
    ) {
        authService.updatePassword(principal.getId(), request);
        return ResponseEntity.ok(new ApiMessageResponse("Parol muvaffaqiyatli yangilandi."));
    }
}
