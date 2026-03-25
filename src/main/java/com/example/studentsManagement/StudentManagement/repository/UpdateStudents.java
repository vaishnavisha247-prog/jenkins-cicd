package com.example.studentsManagement.StudentManagement.repository;

import com.example.studentsManagement.StudentManagement.dto.StudentsDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UpdateStudents {
    void updateStuedents(
            Integer id,
            MultipartFile[] files,
            StudentsDto studentsDto
    ) throws  RuntimeException, IOException;
}
