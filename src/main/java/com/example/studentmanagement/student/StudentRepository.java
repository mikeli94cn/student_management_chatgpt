package com.example.studentmanagement.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByStudentNumber(String studentNumber);
    boolean existsByEmail(String email);

    boolean existsByStudentNumberAndIdNot(String studentNumber, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    Page<Student> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name, String email, Pageable pageable);
}
