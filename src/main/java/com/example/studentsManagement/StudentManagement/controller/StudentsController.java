package com.example.studentsManagement.StudentManagement.controller;


import com.example.studentsManagement.StudentManagement.dto.StudentsDto;
import com.example.studentsManagement.StudentManagement.entity.ImagesEntity;
import com.example.studentsManagement.StudentManagement.entity.StudentsEntity;
import com.example.studentsManagement.StudentManagement.repository.ImageRepository;
import com.example.studentsManagement.StudentManagement.repository.StudentRepository;
import com.example.studentsManagement.StudentManagement.service.StudentsService;
import com.example.studentsManagement.StudentManagement.service.StudentsServiceImp;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentsController {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private StudentsService studentsService;
    @Autowired
    private StudentsServiceImp studentsServiceImp;


    @PostMapping(value = "/create-students-with-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    private ResponseEntity<String> createStudentWithImages(
            @RequestPart("StudentData") String studentDataJson,
            @RequestPart("file") MultipartFile[] file
    ) {
        if (file == null || file.length == 0) {
            return ResponseEntity.badRequest().body("the uploaded file empty");
        }
        try {
            StudentsDto studentsDto = objectMapper.readValue(studentDataJson, StudentsDto.class);
            studentsService.createStudentsWithImage(studentsDto, List.of(file));
            return ResponseEntity.ok("Student recorded and images uploaded successfully");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @GetMapping("/download/{filename}")

    public ResponseEntity<Resource> downloadImage(@PathVariable String filename, HttpServletRequest request) throws IOException {

       Resource resource = (Resource) studentsServiceImp.downloadByFilename(filename);
       String contentType = null;
       try{
           contentType = request.getServletContext()
                   .getMimeType(resource.getFile().getAbsolutePath());
       }catch (IOException e){
           e.printStackTrace();
       }
       if( contentType == null){
           contentType = "application/octet-stream";
       }
       return ResponseEntity.ok()
               .contentType(MediaType.parseMediaType(contentType))
               .header(HttpHeaders.CONTENT_DISPOSITION,
                       "attachment; filename=\"" + filename + "\"")
               .body(resource);

    }






    @GetMapping("/get-all")
    public ResponseEntity<List<StudentsDto>> getAllStudents() {

        List<StudentsEntity> students = studentRepository.findAll();
        List<StudentsDto> responseList = new ArrayList<>();

        for (StudentsEntity s : students) {


            List<ImagesEntity> images = imageRepository.findByStudentsEntity(s.getId());

            StudentsDto dto = new StudentsDto();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setEmail(s.getEmail());
            dto.setAge(s.getAge());
            dto.setFee(s.getFee());
            dto.setGender(s.getGender());
            dto.setMarks(s.getMarks());
            dto.setStandard(s.getStandard());

            List<String> fileNames = new ArrayList<>();
            List<String> filePaths = new ArrayList<>();
            List<String> fileTypes = new ArrayList<>();
            List<String> urls = new ArrayList<>();

            for (ImagesEntity img : images) {
                fileNames.add(img.getFileName());
                filePaths.add(img.getFilePath());
                fileTypes.add(img.getFileType());
                urls.add("http://localhost:8080/students/download/" + img.getFileName());
            }

            dto.setFileName(fileNames);
            dto.setFilePath(filePaths);
            dto.setFileType(fileTypes);
            dto.setImageDownloadUrl(String.valueOf(urls)); // agar DTO me hai

            responseList.add(dto);
        }

        return ResponseEntity.ok(responseList);
    }



    /*@GetMapping("/get-all")
    public ResponseEntity<List<StudentsDto>> getAllStudents(){
        List<StudentsEntity> students  = studentRepository.findAll();
        imageRepository.findByFilePath("http://localhost:8080/students/download/");
        List<StudentsDto> responseList = new ArrayList<>();
        for(StudentsEntity s : students){
            List<ImagesEntity> image = imageRepository.findByIdAndStudentsEntity(s.getId(),s.getImages());
            StudentsDto dto = new StudentsDto();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setEmail(s.getEmail());
            dto.setAge(s.getAge());
            dto.setFee(s.getFee());
            dto.setGender(s.getGender());
            dto.setMarks(s.getMarks());
            dto.setStandard(s.getStandard());
            List<String> fileNames = new ArrayList<>();
            List<String> filePaths = new ArrayList<>();
            List<String> fileType = new ArrayList<>();
            List<String> urls  = new ArrayList<>();
            for(ImagesEntity img : image){
                fileNames.add(img.getFileName());
                filePaths.add(img.getFilePath());
                fileType.add(img.getFileType());
                urls.add("http://localhost:8080/students/download/"+ img.getFileName());
            }
            dto.setFileName(fileNames);
            dto.setFilePath(filePaths);
            dto.setFileType(fileType);

            responseList.add(dto);

        }
        return ResponseEntity.ok(responseList);

    }*/




    @GetMapping("stu/{dto}/{id}")
    public ResponseEntity<StudentsDto> getStudentDto(@PathVariable Integer id) {
        return ResponseEntity.ok(studentsService.getStudentDtoById(id));
    }



}

