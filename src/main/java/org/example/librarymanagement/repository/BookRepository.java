package org.example.librarymanagement.repository;

import org.example.librarymanagement.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("UPDATE Book b SET b.availableCopies = b.availableCopies - 1 " +
            "WHERE b.id = :id AND b.availableCopies > 0")
    int decreaseAvailableCopies(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Book b SET b.availableCopies = b.availableCopies + 1 " +
            "WHERE b.id = :id AND b.availableCopies < b.totalCopies")
    int increaseAvailableCopies(@Param("id") Long id);
}