package com.example.studentsManagement.StudentManagement.repository;

import com.example.studentsManagement.StudentManagement.entity.StudentsEntity;

public interface DeleteImage {
    void deleteImageByFileNam(
            String filename,
            StudentsEntity students
    ) throws RuntimeException;
}
