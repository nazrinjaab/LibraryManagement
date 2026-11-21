package org.example.librarymanagement.service;

import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BookRequest;
import org.example.librarymanagement.dto.response.BookResponse;
import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.exception.BusinessException;
import org.example.librarymanagement.exception.NotFoundException;
import org.example.librarymanagement.repository.BookRepository;
import org.example.librarymanagement.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public BookResponse create(BookRequest request) {
        if (bookRepository.existsByIsbn(request.isbn())) {
            throw new BusinessException("error.book.isbnExists", request.isbn());
        }

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.totalCopies());

        Book saved = bookRepository.save(book);
        return toResponse(saved);
    }

    @Transactional
    public BookResponse update(Long id, BookRequest request){
        Book book = bookRepository.findByIdForUpdate(id)
                .orElseThrow(()-> new NotFoundException("error.book.notFound", id));

        int borrowed = book.getTotalCopies() - book.getAvailableCopies();
        if (request.totalCopies() < borrowed){
            throw new BusinessException("error.book.totalLessThanBorrowed", borrowed);
        }
        if (bookRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
            throw new BusinessException("error.book.isbnExists", request.isbn());
        }
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setTotalCopies(request.totalCopies());
        book.setAvailableCopies(request.totalCopies() - borrowed);

        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public BookResponse getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("error.book.notFound", id));
        return toResponse(book);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id){
        if(!bookRepository.existsById(id)){
            throw new NotFoundException("error.book.notFound", id);
        }
        if(loanRepository.existsByBookId(id)){
            throw new BusinessException("error.book.hasLoans", id);
        }
        bookRepository.deleteById(id);
    }

    private BookResponse toResponse(Book book) {
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }

}