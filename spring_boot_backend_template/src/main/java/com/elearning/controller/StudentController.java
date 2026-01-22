package com.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.dtos.UpdateProfileDTO;
import com.elearning.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final StudentService studentService;

    // ===============================
    // GET STUDENT PROFILE
    // ===============================
    @GetMapping("/profile")
    public ResponseEntity<StudentResponseDTO> getProfile() {
        return ResponseEntity.ok(studentService.getProfile());
    }

    // ===============================
    // GET ALL COURSES
    // ===============================
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(studentService.getAllCourses());
    }

    // ===============================
    // ENROLL COURSE (JWT BASED)
    // ===============================
    @PostMapping("/enroll")
    public ResponseEntity<String> enrollCourse(
            @RequestParam Long courseId) {

        return ResponseEntity.ok(
                studentService.enrollCourse(courseId)
        );
    }

    // ===============================
    // GET MY COURSES (JWT BASED)
    // ===============================
    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseResponseDTO>> getMyCourses() {

        return ResponseEntity.ok(
                studentService.getMyCourses()
        );
    }
    
    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> updateProfile(
           @Valid @RequestBody UpdateProfileDTO dto) {

        studentService.updateProfile(dto);
        return ResponseEntity.ok("Profile updated successfully");
    }

    
}
