package com.example.studentsManagement.StudentManagement.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.dialect.function.json.JsonTableSetReturningFunctionTypeResolver;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="ImageTable")
public class ImagesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fileName;
    private String fileType;
    private String filePath;

    @Lob
    @Column(name="data")
    private byte[] data;

    @ManyToOne
    @JoinColumn(name="students_id")
    @JsonBackReference
    private StudentsEntity studentsEntity;



}
