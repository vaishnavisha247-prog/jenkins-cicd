package com.example.studentsManagement.StudentManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="StudentsTable")
public class StudentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String standard;
    @Column(nullable = false)
    private String gender;
    private Integer age;
    private Double fee;
    private Double marks;


    @OneToMany(mappedBy = "studentsEntity",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ImagesEntity> images = new ArrayList<>();

}
