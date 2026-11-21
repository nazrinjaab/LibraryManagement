package org.example.librarymanagement.repository;


import org.example.librarymanagement.entity.Book;
import org.example.librarymanagement.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {



    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
