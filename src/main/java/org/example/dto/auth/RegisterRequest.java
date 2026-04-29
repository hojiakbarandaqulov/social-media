package org.example.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Full name bo'sh bo'lmasligi kerak.")
        @Size(max = 120, message = "Full name 120 belgidan oshmasligi kerak.")
        String fullName,

        @NotBlank(message = "Email bo'sh bo'lmasligi kerak.")
        @Email(message = "Email formati noto'g'ri.")
        @Size(max = 160, message = "Email 160 belgidan oshmasligi kerak.")
        String email,

        @NotBlank(message = "Parol bo'sh bo'lmasligi kerak.")
        @Size(min = 8, max = 100, message = "Parol 8 dan 100 belgigacha bo'lishi kerak.")
        String password
) {
}
