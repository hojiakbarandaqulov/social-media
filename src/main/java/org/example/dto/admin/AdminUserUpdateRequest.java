package org.example.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.enums.Role;
import org.example.enums.UserStatus;

public record AdminUserUpdateRequest(
        @NotBlank(message = "Full name bo'sh bo'lmasligi kerak.")
        @Size(max = 120, message = "Full name 120 belgidan oshmasligi kerak.")
        String fullName,

        @NotBlank(message = "Email bo'sh bo'lmasligi kerak.")
        @Email(message = "Email formati noto'g'ri.")
        @Size(max = 160, message = "Email 160 belgidan oshmasligi kerak.")
        String email,

        @Size(max = 500, message = "Bio 500 belgidan oshmasligi kerak.")
        String bio,

        @NotNull(message = "Role tanlanishi kerak.")
        Role role,

        @NotNull(message = "Status tanlanishi kerak.")
        UserStatus status
) {
}
