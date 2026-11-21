package org.example.librarymanagement.service;

import org.example.librarymanagement.dto.request.LoanRequest;
import org.example.librarymanagement.dto.response.LoanResponse;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.entity.Loan;
import org.example.librarymanagement.entity.Member;
import org.example.librarymanagement.enums.LoanStatus;
import org.example.librarymanagement.exception.BusinessException;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.repository.LoanRepository;
import org.example.librarymanagement.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void borrow_shouldCreateLoan_whenAllRulesPass(){
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Əli və Nino");

        Member member = new Member();
        member.setId(2L);
        member.setFullName("Nazrin");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(loanRepository.countByMemberIdAndStatus(2L, LoanStatus.ACTIVE)).thenReturn(0L);
        when(bookRepository.decreaseAvailableCopies(1L)).thenReturn(1);
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan loan = invocation.getArgument(0);
            loan.setId(10L);
            return loan;
        });

        LoanResponse response = loanService.borrow(new LoanRequest(1L,2L));

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.bookTitle()).isEqualTo("Əli və Nino");
        assertThat(response.status()).isEqualTo(LoanStatus.ACTIVE);
        assertThat(response.loanDate()).isEqualTo(LocalDate.now());
        verify(bookRepository).decreaseAvailableCopies(1L);

    }

    @Test
    void borrow_shouldThrow_whenMemberHasMaxActiveLoans(){
        Book book = new Book();
        book.setId(1L);

        Member member = new Member();
        member.setId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(loanRepository.countByMemberIdAndStatus(2L, LoanStatus.ACTIVE)).thenReturn(3L);

        assertThatThrownBy(() -> loanService.borrow(new LoanRequest(1L,2L)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("error.member.loanLimit");

        verify(bookRepository, never()).decreaseAvailableCopies(any());
        verify(loanRepository, never()).save(any());
    }

    @Test
    void borrow_shouldThrow_whenBookOutOfStock(){
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test kitab");

        Member member = new Member();
        member.setId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.findById(2L)).thenReturn(Optional.of(member));
        when(loanRepository.countByMemberIdAndStatus(2L,LoanStatus.ACTIVE)).thenReturn(0L);
        when(bookRepository.decreaseAvailableCopies(1L)).thenReturn(0);

        assertThatThrownBy(()-> loanService.borrow(new LoanRequest(1L,2L)))
                .isInstanceOf(BusinessException.class)
                .hasMessage("error.book.outOfStock");

        verify(loanRepository, never()).save(any());
    }

    @Test
    void returnBook_shouldMarkAsReturned_whenLoanIsActive(){
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test kitab");

        Member member = new Member();
        member.setId(2L);
        member.setFullName("Nazrin");

        Loan loan = new Loan();
        loan.setId(10L);
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loan.setStatus(LoanStatus.ACTIVE);

        when(loanRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(loan));

        LoanResponse response = loanService.returnBook(10L);

        assertThat(response.status()).isEqualTo(LoanStatus.RETURNED);
        assertThat(response.returnDate()).isEqualTo(LocalDate.now());
        verify(bookRepository).increaseAvailableCopies(1L);
    }

    @Test
    void returnBook_shouldThrow_whenAlreadyReturned(){
        Loan loan = new Loan();
        loan.setId(10L);
        loan.setStatus(LoanStatus.RETURNED);

        when(loanRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(loan));

        assertThatThrownBy(()->loanService.returnBook(10L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("error.loan.alreadyReturned");
        verify(bookRepository, never()).increaseAvailableCopies(any());
    }
}
