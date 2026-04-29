package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.user.ProfileResponse;
import org.example.dto.user.UpdateProfileRequest;
import org.example.security.CustomUserPrincipal;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "Profile", description = "Foydalanuvchi profili bilan ishlash")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Joriy foydalanuvchi profilini olish")
    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return ResponseEntity.ok(userService.getCurrentProfile(principal.getId()));
    }

    @Operation(summary = "Joriy foydalanuvchi profil ma'lumotlarini yangilash")
    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userService.updateCurrentProfile(principal.getId(), request));
    }

    @Operation(summary = "Joriy foydalanuvchi avatarini yuklash")
    @PatchMapping("/me/avatar")
    public ResponseEntity<ProfileResponse> updateAvatar(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(userService.updateAvatar(principal.getId(), file));
    }
}
