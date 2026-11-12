package com.example.studentmanagement.student;

public record StudentResponse(
        Long id,
        String studentNumber,
        String name,
        String email,
        Integer age,
        String major
) {
    public static StudentResponse from(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getStudentNumber(),
                student.getName(),
                student.getEmail(),
                student.getAge(),
                student.getMajor()
        );
    }
}
