package org.example.librarymanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.LoanRequest;
import org.example.librarymanagement.dto.response.LoanResponse;
import org.example.librarymanagement.service.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor

public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse borrow(@Valid @RequestBody LoanRequest request){
        return loanService.borrow(request);
    }

    @PostMapping("/{id}/return")
    public LoanResponse returnBook (@PathVariable Long id){
        return loanService.returnBook(id);
    }
}
