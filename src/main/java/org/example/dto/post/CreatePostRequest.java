package org.example.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank(message = "Post matni bo'sh bo'lmasligi kerak.")
        @Size(max = 2000, message = "Post matni 2000 belgidan oshmasligi kerak.")
        String content
) {
}
