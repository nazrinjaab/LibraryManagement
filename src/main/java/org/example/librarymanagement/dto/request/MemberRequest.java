package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank(message = "{validation.notBlank}")
        String fullName,

        @NotBlank(message = "{validation.notBlank}")
        @Email(message = "{validation.email}")
        String email
) {
}
