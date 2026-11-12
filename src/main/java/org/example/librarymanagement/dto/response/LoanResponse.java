package org.example.librarymanagement.dto.response;

import org.example.librarymanagement.enums.LoanStatus;

import java.time.LocalDate;

public record LoanResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long memberId,
        String memberFullName,
        LocalDate loanDate,
        LocalDate returnDate,
        LoanStatus status
) {
}
