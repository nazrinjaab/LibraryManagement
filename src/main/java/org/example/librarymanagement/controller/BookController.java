package org.example.librarymanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.BookRequest;
import org.example.librarymanagement.dto.response.BookResponse;

import org.example.librarymanagement.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse create(@Valid @RequestBody BookRequest request) {

        return bookService.create(request);
    }

    @PutMapping("/{id}")
    public BookResponse update (@PathVariable Long id, @Valid @RequestBody BookRequest request){
        return bookService.update(id, request);
    }

    @GetMapping("/{id}")
    public BookResponse getById(@PathVariable Long id) {

        return bookService.getById(id);
    }

    @GetMapping
    public List<BookResponse> getAll() {

        return bookService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){

        bookService.delete(id);
    }
}
