package org.example.librarymanagement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.MemberRequest;
import org.example.librarymanagement.dto.response.MemberResponse;
import org.example.librarymanagement.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MemberResponse create(@Valid @RequestBody MemberRequest request){
        return memberService.create(request);
    }

    @GetMapping("/{id}")
    public MemberResponse getById(@PathVariable Long id){
        return memberService.getById(id);
    }

    @GetMapping
    public List<MemberResponse> getAll(){
        return memberService.getAll();
    }
}
