package org.example.librarymanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoanRequest(
        @NotNull(message = "{validation.notNull}")
        Long bookId,

        @NotNull(message = "{validation.notNull}")
        Long memberId
) {
}
