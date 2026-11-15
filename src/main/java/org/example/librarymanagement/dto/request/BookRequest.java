package org.example.librarymanagement.dto.request;

import jakarta.validation.Validation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(

        @NotBlank(message = "{validation.notBlank}")
        String title,

        @NotBlank(message = "{validation.notBlank}")
        String author,

        @NotBlank(message = "{validation.notBlank}")
        @Size(max = 20, message = "{validation.size}")
        String isbn,

        @NotNull(message = "{validation.notNull}")
        @Min(value=1, message = "{validation.min}")
        Integer totalCopies
) {
}
