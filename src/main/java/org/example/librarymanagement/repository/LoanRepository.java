package org.example.librarymanagement.repository;

import jakarta.persistence.LockModeType;
import org.example.librarymanagement.entity.Loan;
import org.example.librarymanagement.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    long countByMemberIdAndStatus(Long memberId, LoanStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM Loan l WHERE l.id = :id")
    Optional<Loan> findByIdForUpdate(@Param("id") Long id);
}
