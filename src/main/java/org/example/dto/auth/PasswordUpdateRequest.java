package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
        @NotBlank(message = "Joriy parol bo'sh bo'lmasligi kerak.")
        String currentPassword,

        @NotBlank(message = "Yangi parol bo'sh bo'lmasligi kerak.")
        @Size(min = 8, max = 100, message = "Yangi parol 8 dan 100 belgigacha bo'lishi kerak.")
        String newPassword
) {
}
