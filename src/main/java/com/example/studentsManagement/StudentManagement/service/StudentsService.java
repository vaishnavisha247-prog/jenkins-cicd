package com.example.studentsManagement.StudentManagement.service;

import com.example.studentsManagement.StudentManagement.dto.StudentsDto;
import com.example.studentsManagement.StudentManagement.entity.ImagesEntity;
import com.example.studentsManagement.StudentManagement.entity.StudentsEntity;
import com.example.studentsManagement.StudentManagement.repository.DeleteImage;
import com.example.studentsManagement.StudentManagement.repository.ImageRepository;
import com.example.studentsManagement.StudentManagement.repository.StudentRepository;
import com.example.studentsManagement.StudentManagement.repository.UpdateStudents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ssl.SslProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.util.NativeImageUtil;

import java.awt.image.AreaAveragingScaleFilter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentsService implements DeleteImage , UpdateStudents {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ImageRepository imageRepository;


    public static final String STORAGE_DIRECTORY = "C:/Users/VAISHNAVI SHARMA/Desktop/file_storage/";


    public String saveFileToStorage(MultipartFile file) throws IOException {

        String uploadDir = "C:/Users/VAISHNAVI SHARMA/OneDrive/Desktop/file_storage/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String originalName = file.getOriginalFilename();
        String ext = "";

        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String uniqueFile = UUID.randomUUID() + ext;

        File deskFile = new File(directory + uniqueFile);
        file.transferTo(deskFile);
        return uniqueFile;

    }


    public void createStudentsWithImage(StudentsDto studentsDto, List<MultipartFile> file) {
        try {
            StudentsEntity studentsEntity = new StudentsEntity();
            studentsEntity.setName(studentsDto.getName());
            studentsEntity.setEmail(studentsDto.getEmail());
            studentsEntity.setAge(studentsDto.getAge());
            studentsEntity.setGender(studentsDto.getGender());
            studentsEntity.setFee(studentsDto.getFee());
            studentsEntity.setMarks(studentsDto.getMarks());
            studentsEntity.setStandard(studentsDto.getStandard());

            StudentsEntity savedStudents = studentRepository.save(studentsEntity);

            for (MultipartFile f : file) {
                String savedFile = saveFileToStorage(f);

                ImagesEntity images = new ImagesEntity();
                images.setFileName(savedFile);
                images.setFileType(f.getContentType());
                images.setFilePath(STORAGE_DIRECTORY + savedFile);
                images.setStudentsEntity(savedStudents);
                imageRepository.save(images);

            }


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving images: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Something went wrong: " + e.getMessage());
        }
    }


    public StudentsDto getStudentDtoById(Integer id) {
        StudentsEntity student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<ImagesEntity> images = imageRepository.findByStudentsEntity(student.getId());

        StudentsDto dto = new StudentsDto();

        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setStandard(student.getStandard());
        dto.setGender(student.getGender());
        dto.setAge(student.getAge());
        dto.setFee(student.getFee());
        dto.setMarks(student.getMarks());

        List<String> fileName = new ArrayList<>();

        for (ImagesEntity img : images) {
            fileName.add(img.getFileName());
        }
        dto.setFileName(fileName);

        List<String> imageUrls = new ArrayList<>();
        for (ImagesEntity img : images) {
            imageUrls.add(

                    "http://localhost:8080/students/download/" + img.getFileName()
            );
        }
        dto.setImageDownloadUrl(String.valueOf(imageUrls));
        return dto;
    }


    public File getDownloadFile(String fileName) throws Exception {
        System.out.println("Recived name: " + fileName);

        if (fileName == null) {
            throw new RuntimeException("file not found: ");
        }
        System.out.println("STORAGE_DIRECTORY" + STORAGE_DIRECTORY);
        File fileToDownload = new File(STORAGE_DIRECTORY, fileName);
        System.out.println("full path" + fileToDownload.getAbsoluteFile());
        System.out.println("file exists" + fileToDownload.exists());
        System.out.println("is file" + fileToDownload.isFile());
        System.out.println("can read " + fileToDownload.canRead());
        File parentDir = fileToDownload.getParentFile();
        System.out.println("parant director" + parentDir.getParentFile());
        System.out.println("directory path" + parentDir.getParentFile());
        if (!fileToDownload.exists()) {
            throw new RuntimeException("no file found : " + fileToDownload.getAbsoluteFile());
        }
        return fileToDownload;
    }


    @Transactional

    public ResponseEntity<StudentsDto> getAllStudentsWithImageUrl() {
        List<StudentsEntity> studentsEntities = studentRepository.findAll();
        List<StudentsDto> response = new ArrayList<>();
        for (StudentsEntity students : studentsEntities) {
            StudentsDto studentsDto = new StudentsDto();
            studentsDto.setMarks(students.getMarks());
            studentsDto.setFee(students.getFee());
            studentsDto.setAge(students.getAge());
            studentsDto.setStandard(students.getStandard());
            studentsDto.setId(students.getId());
            studentsDto.setName(students.getName());
            List<ImagesEntity> imagesEntities = imageRepository.findByStudentsEntity(students.getId());
            List<String> urls = new ArrayList<>();
            for (ImagesEntity images : imagesEntities) {
                urls.add("http://localhost:8080/students/download-faster/" + images.getFileName());
            }
            studentsDto.setImageDownloadUrl(String.valueOf(urls));
            response.add(studentsDto);
        }
        return (ResponseEntity<StudentsDto>) response;
    }


    /*@Transactional

    public void updateStudents(Integer id, MultipartFile[] file, StudentsDto studentsDto) throws IOException {
        StudentsEntity studentsEntity = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("file not found"));
        studentsEntity.setName(studentsDto.getName());
        studentsEntity.setAge(studentsDto.getAge());
        studentsEntity.setMarks(studentsDto.getMarks());
        studentsEntity.setGender(studentsDto.getGender());
        studentsEntity.setFee(studentsDto.getFee());
        studentsEntity.setEmail(studentsDto.getEmail());
        if (file != null && file.length > 0) {
            for (MultipartFile f : file) {
                String originalName = f.getOriginalFilename();
                String ext = originalName.substring(originalName.lastIndexOf("."));
                String UUIDName = UUID.randomUUID() + ext;
                Path filePath = Paths.get(STORAGE_DIRECTORY, UUIDName);
                Files.copy(f.getInputStream(), filePath);
                ImagesEntity images = new ImagesEntity();
                images.setFileName(UUIDName);
                images.setFileType(f.getContentType());
                images.setFilePath(filePath.toString());
                images.setStudentsEntity(studentsEntity);
                studentsEntity.getImages().add(images);

            }
            studentRepository.save(studentsEntity);
        }
    }*/



    public boolean deleteStudents(Integer id) {

        try {
            StudentsEntity students = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("file not found"));
            studentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void deleteImageByFileNam(String filename, StudentsEntity students) throws RuntimeException {

    }

    @Override
    @Transactional
    public void updateStuedents(Integer id, MultipartFile[] files, StudentsDto studentsDto) throws IOException {
        StudentsEntity studentsEntity = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("file not found"));
        studentsEntity.setEmail(studentsDto.getEmail());
        if(files != null && files.length>0){
            for(MultipartFile f: files){
                String originalName = f.getOriginalFilename();
                String ext = originalName.substring(originalName.lastIndexOf("."));
                String Uuid = UUID.randomUUID() + ext;
                Path filepath = Paths.get(STORAGE_DIRECTORY + Uuid);
                Files.copy(f.getInputStream(),filepath);
                ImagesEntity images = new ImagesEntity();
                images.setFileName(Uuid);
                images.setFilePath(filepath.toString());
                images.setFileType(f.getContentType());
                images.setStudentsEntity(studentsEntity);
                studentsEntity.getImages().add(images);

            }
            studentRepository.save(studentsEntity);
        }

    }
}


/*public String uploadImage(MultipartFile file) throws IOException {

    imageRepository.save(Image.builder()
            .fileName(file.getOriginalFilename())
            .fileType(file.getContentType())
            .data(ImageUtil.compressImage(file.getBytes()))
            .build());
    return "file upload successfully: " + file.getOriginalFilename();
}*/


/*@Transactional
public List<StudentsServiceDto> getAllStudentsWithImageURl() {
    List<Students> students = studentsRepository.findAll();
    List<StudentsServiceDto> responseList = new ArrayList<>();
    for (Students students1 : students) {
        //List<Image> image = Collections.singletonList((Image) imageRepository.findByStudentId(students1.getId()));
        StudentsServiceDto Dto = new StudentsServiceDto();
        Dto.setName(students1.getName());
        Dto.setId(students1.getId());
        Dto.setAge(students1.getAge());
        Dto.setEmail(students1.getEmail());
        Dto.setMarks(students1.getMarks());
        Dto.setStandard(students1.getStandard());
        Dto.setFees(students1.getFees());

        //String fileName = students1.getImagePath();
        List<Image> image = imageRepository.findByStudentId(students1.getId());

        List<String> urls = new ArrayList<>();
        for (Image image1 : image) {
            urls.add("http://localhost:8080/students/download-faster/" + image1.getFileName());

        }
        Dto.setImageDownloadUrl(urls);
        responseList.add(Dto);
        }
        return responseList;
        }
 */














