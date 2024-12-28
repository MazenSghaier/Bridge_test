package com.example.Courses_management.service;

import com.example.Courses_management.dto.CourseDTO;
import com.example.Courses_management.entity.Course;
import com.example.Courses_management.exception.FileStorageException;
import com.example.Courses_management.exception.ResourceNotFoundException;
import com.example.Courses_management.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CourseService {
    private final CourseRepository courseRepository;
    private final String uploadDirectory = "uploads/courses/";

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CourseDTO getCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        return convertToDTO(course);
    }

    public CourseDTO createCourse(CourseDTO courseDTO, MultipartFile image) {
        Course course = new Course();
        updateCourseFromDTO(course, courseDTO);

        if (image != null && !image.isEmpty()) {
            String fileName = handleImageUpload(image);
            course.setImageUrl(fileName);
        }

        Course savedCourse = courseRepository.save(course);
        return convertToDTO(savedCourse);
    }

    public CourseDTO updateCourse(Long id, CourseDTO courseDTO, MultipartFile image) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        updateCourseFromDTO(course, courseDTO);

        if (image != null && !image.isEmpty()) {
            if (course.getImageUrl() != null) {
                deleteImage(course.getImageUrl());
            }
            String fileName = handleImageUpload(image);
            course.setImageUrl(fileName);
        }

        Course updatedCourse = courseRepository.save(course);
        return convertToDTO(updatedCourse);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (course.getImageUrl() != null) {
            deleteImage(course.getImageUrl());
        }

        courseRepository.delete(course);
    }

    private String handleImageUpload(MultipartFile image) {
        try {
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file. Please try again!", e);
        }
    }

    private void deleteImage(String fileName) {
        try {
            Path filePath = Paths.get(uploadDirectory).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Error deleting image: {}", e.getMessage());
        }
    }

    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setDuration(course.getDuration());
        dto.setImageUrl(course.getImageUrl());
        return dto;
    }

    private void updateCourseFromDTO(Course course, CourseDTO dto) {
        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());
        course.setDuration(dto.getDuration());
    }
}