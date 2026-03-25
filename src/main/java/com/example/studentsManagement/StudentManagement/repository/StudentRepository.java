package com.example.studentsManagement.StudentManagement.repository;

import com.example.studentsManagement.StudentManagement.entity.StudentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentsEntity ,Integer> {
    StudentsEntity findByName(String name);
}
