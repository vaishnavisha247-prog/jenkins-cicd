package com.example.studentsManagement.StudentManagement.repository;

import com.example.studentsManagement.StudentManagement.entity.ImagesEntity;
import com.example.studentsManagement.StudentManagement.entity.StudentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ImagesEntity, Integer> {
    List<ImagesEntity> findByFilePath(String filePath);

    List<ImagesEntity> findByIdAndStudentsEntity(Integer id , StudentsEntity students);

    List<ImagesEntity> findByStudentsEntity(Integer id);
    List<ImagesEntity> findByFileNameAndStudentsEntity(String fileName , StudentsEntity students);
}
