package org.example.librarymanagement.service;

import org.example.librarymanagement.exception.BusinessException;
import org.example.librarymanagement.repository.LoanRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.librarymanagement.dto.request.MemberRequest;
import org.example.librarymanagement.dto.response.MemberResponse;
import org.example.librarymanagement.entity.Member;
import org.example.librarymanagement.exception.NotFoundException;
import org.example.librarymanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final LoanRepository loanRepository;

    @Transactional
    public MemberResponse create(MemberRequest request){
        if(memberRepository.existsByEmail(request.email())){
            throw new BusinessException("error.member.emailExists", request.email());
        }
        Member member = new Member();
        member.setFullName(request.fullName());
        member.setEmail(request.email());

        Member saved = memberRepository.save(member);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MemberResponse getById(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("error.member.notFound", id));
        return toResponse(member);
    }

    @Transactional
    public MemberResponse update( Long id, MemberRequest request){
        Member member = memberRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("error.member.notFound", id));

        if (memberRepository.existsByEmailAndIdNot(request.email(), id)){
            throw new BusinessException("error.member.emailExists", request.email());
        }
        member.setFullName(request.fullName());
        member.setEmail(request.email());

        return toResponse(member);
    }


    @Transactional(readOnly = true)
    public List<MemberResponse> getAll(){
        return memberRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id){
        if (!memberRepository.existsById(id)){
            throw new NotFoundException("error.member.notFound", id);
        }
        if(loanRepository.existsByMemberId(id)){
            throw new BusinessException("error.member.hasLoans", id);
        }
        memberRepository.deleteById(id);
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getFullName(),
                member.getEmail()
        );
    }
}
