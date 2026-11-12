package com.example.studentmanagement.student;

import com.example.studentmanagement.common.ConflictException;
import com.example.studentmanagement.common.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public StudentResponse create(StudentRequest request) {
        ensureUnique(request.studentNumber(), request.email(), null);
        Student student = new Student(
                request.studentNumber(), request.name(), request.email(),
                request.age(), request.major());
        return StudentResponse.from(repository.save(student));
    }

    @Transactional(readOnly = true)
    public StudentResponse getById(Long id) {
        return StudentResponse.from(findStudent(id));
    }

    @Transactional(readOnly = true)
    public Page<StudentResponse> list(String keyword, Pageable pageable) {
        Page<Student> page;
        if (keyword == null || keyword.isBlank()) {
            page = repository.findAll(pageable);
        } else {
            String term = keyword.trim();
            page = repository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    term, term, pageable);
        }
        return page.map(StudentResponse::from);
    }

    public StudentResponse update(Long id, StudentRequest request) {
        Student student = findStudent(id);
        ensureUnique(request.studentNumber(), request.email(), id);
        student.update(request.studentNumber(), request.name(), request.email(),
                request.age(), request.major());
        return StudentResponse.from(repository.save(student));
    }

    public void delete(Long id) {
        Student student = findStudent(id);
        repository.delete(student);
    }

    private Student findStudent(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student with id " + id + " was not found"));
    }

    private void ensureUnique(String studentNumber, String email, Long currentId) {
        boolean numberExists = currentId == null
                ? repository.existsByStudentNumber(studentNumber)
                : repository.existsByStudentNumberAndIdNot(studentNumber, currentId);
        if (numberExists) {
            throw new ConflictException("studentNumber already exists: " + studentNumber);
        }

        boolean emailExists = currentId == null
                ? repository.existsByEmail(email)
                : repository.existsByEmailAndIdNot(email, currentId);
        if (emailExists) {
            throw new ConflictException("email already exists: " + email);
        }
    }
}
