package com.example.studentsManagement.StudentManagement.dto;


import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentsDto {

    private Integer id;
    private  String name;
    private String email;
    private String standard;
    private Integer age;
    private  Double fee;
    private Double marks;
    private String gender;


    private List<String> fileName;
    private List<String> filePath;
    private List<String> fileType;
    private String ImageDownloadUrl;


}
