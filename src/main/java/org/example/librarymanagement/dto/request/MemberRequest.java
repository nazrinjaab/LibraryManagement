package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @NotBlank
        String fullName,

        @NotBlank
        @Email
        String email
) {
}
