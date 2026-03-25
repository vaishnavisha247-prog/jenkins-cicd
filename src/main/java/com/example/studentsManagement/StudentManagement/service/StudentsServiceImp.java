package com.example.studentsManagement.StudentManagement.service;

import com.example.studentsManagement.StudentManagement.repository.StudentsService;
import jakarta.annotation.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StudentsServiceImp implements StudentsService {
    private static final String STORAGE_DIR = "C:/Users/VAISHNAVI SHARMA/OneDrive/Desktop/file_storage/";

    @Override
    public Resource downloadByFilename(String filename) {
        try{
            Path filePath = Paths.get(STORAGE_DIR)
                    .resolve(filename)
                    .normalize();

            UrlResource resource = new UrlResource(filePath.toUri());
            if(resource.exists() && resource.isReadable()){
                return (Resource) resource;
            }
            else {
                throw  new RuntimeException("file not found: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("error while reading file" + e);
        }
    }
}

