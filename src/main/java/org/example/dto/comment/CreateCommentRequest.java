package org.example.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(
        @NotBlank(message = "Comment matni bo'sh bo'lmasligi kerak.")
        @Size(max = 1000, message = "Comment matni 1000 belgidan oshmasligi kerak.")
        String content
) {
}
