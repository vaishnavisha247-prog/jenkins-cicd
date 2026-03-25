package com.example.studentsManagement.StudentManagement.repository;


import jakarta.annotation.Resource;

public interface StudentsService {
    Resource downloadByFilename(String Filename);
}
