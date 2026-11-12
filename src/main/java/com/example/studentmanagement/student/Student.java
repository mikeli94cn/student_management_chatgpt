package com.example.studentmanagement.student;

import jakarta.persistence.*;

@Entity
@Table(name = "students",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_student_number", columnNames = "student_number"),
           @UniqueConstraint(name = "uk_student_email", columnNames = "email")
       })
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_number", nullable = false, length = 30)
    private String studentNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 100)
    private String major;

    protected Student() {
        // Required by JPA.
    }

    public Student(String studentNumber, String name, String email, Integer age, String major) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.age = age;
        this.major = major;
    }

    public Long getId() { return id; }
    public String getStudentNumber() { return studentNumber; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Integer getAge() { return age; }
    public String getMajor() { return major; }

    public void update(String studentNumber, String name, String email, Integer age, String major) {
        this.studentNumber = studentNumber;
        this.name = name;
        this.email = email;
        this.age = age;
        this.major = major;
    }
}
