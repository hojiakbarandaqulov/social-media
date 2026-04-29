package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.admin.AdminUserResponse;
import org.example.dto.admin.AdminUserUpdateRequest;
import org.example.dto.api.ApiMessageResponse;
import org.example.security.CustomUserPrincipal;
import org.example.service.AdminUserManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "Admin Users", description = "Admin tomonidan userlarni boshqarish")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserManagementService adminUserManagementService;

    public AdminUserController(AdminUserManagementService adminUserManagementService) {
        this.adminUserManagementService = adminUserManagementService;
    }

    @Operation(summary = "Barcha foydalanuvchilar ro'yxatini olish")
    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> getUsers() {
        return ResponseEntity.ok(adminUserManagementService.getAllUsers());
    }

    @Operation(summary = "Bitta foydalanuvchini ID bo'yicha olish")
    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserResponse> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(adminUserManagementService.getUserById(userId));
    }

    @Operation(summary = "Foydalanuvchi ma'lumotlari, role va statusini yangilash")
    @PatchMapping("/{userId}")
    public ResponseEntity<AdminUserResponse> updateUser(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long userId,
            @Valid @RequestBody AdminUserUpdateRequest request
    ) {
        return ResponseEntity.ok(adminUserManagementService.updateUser(principal.getId(), userId, request));
    }

    @Operation(summary = "Foydalanuvchini bloklash")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiMessageResponse> deactivateUser(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long userId
    ) {
        adminUserManagementService.deactivateUser(principal.getId(), userId);
        return ResponseEntity.ok(new ApiMessageResponse("Foydalanuvchi bloklandi."));
    }
}
