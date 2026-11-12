package org.example.librarymanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.LoanRequest;
import org.example.librarymanagement.dto.response.LoanResponse;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.entity.Loan;
import org.example.librarymanagement.entity.Member;
import org.example.librarymanagement.enums.LoanStatus;
import org.example.librarymanagement.exception.BusinessException;
import org.example.librarymanagement.exception.NotFoundException;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.repository.LoanRepository;
import org.example.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor

public class LoanService {

    private static final int MAX_ACTIVE_LOANS = 3;

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public LoanResponse borrow(LoanRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new NotFoundException("Book not found:" + request.bookId()));

        Member member = memberRepository.findById((request.memberId()))
                .orElseThrow(() -> new NotFoundException("Member not found:" + request.memberId()));

        long activeLoans = loanRepository.countByMemberIdAndStatus(member.getId(), LoanStatus.ACTIVE);
        if (activeLoans >= MAX_ACTIVE_LOANS) {
            throw new BusinessException(("Member already has " + MAX_ACTIVE_LOANS + " active loans"));
        }

        int updated = bookRepository.decreaseAvailableCopies(book.getId());
        if (updated == 0) {
            throw new BusinessException("Book is out of stock: " + book.getTitle());
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE);

        Loan saved = loanRepository.save(loan);
        return toResponse(saved);
    }

    @Transactional
    public LoanResponse returnBook(Long loanId) {

        Loan loan = loanRepository.findByIdForUpdate(loanId)
                .orElseThrow(() -> new NotFoundException("Loan not found: " + loanId));
        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new BusinessException("Loan already returned: " + loanId);
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());

        bookRepository.increaseAvailableCopies(loan.getBook().getId());

        return toResponse(loan);
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getMember().getId(),
                loan.getMember().getFullName(),
                loan.getLoanDate(),
                loan.getReturnDate(),
                loan.getStatus()

        );
    }
}



