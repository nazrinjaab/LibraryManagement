package org.example.librarymanagement.dto.response;

public record MemberResponse(
        Long id,
        String fullName,
        String email
) {
}
