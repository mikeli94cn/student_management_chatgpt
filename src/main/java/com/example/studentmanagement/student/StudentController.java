package com.example.studentmanagement.student;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse create(@Valid @RequestBody StudentRequest request) {
        return service.create(request);
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Page<StudentResponse> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        return service.list(keyword,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    @PutMapping("/{id}")
    public StudentResponse update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
