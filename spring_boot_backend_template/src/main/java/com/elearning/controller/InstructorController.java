package com.elearning.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.elearning.dtos.CourseRequestDTO;
import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.QuizRequestDTO;
import com.elearning.dtos.QuizResponseDTO;
import com.elearning.service.InstructorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorController {

    private final InstructorService instructorService;

    // ✅ CREATE COURSE (NO instructorId)
    @PostMapping("/course")
    public ResponseEntity<CourseResponseDTO> createCourse(
            @RequestBody CourseRequestDTO dto,
            Principal principal
    ) {
        return ResponseEntity.ok(
                instructorService.createCourse(principal.getName(), dto)
        );
    }

    // ✅ GET MY COURSES
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponseDTO>> getMyCourses(
            Principal principal
    ) {
        return ResponseEntity.ok(
                instructorService.getCoursesByEmail(principal.getName())
        );
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizResponseDTO> createQuiz(
            @RequestBody QuizRequestDTO dto
    ) {
        return ResponseEntity.ok(
                instructorService.createQuiz(dto)
        );
    }
        
        
        
        // ✅ DELETE COURSE
        @DeleteMapping("/course/{courseId}")
        public ResponseEntity<Void> deleteCourse(
                @PathVariable Long courseId,
                Principal principal
        ) {
            instructorService.deleteCourse(courseId, principal.getName());
            return ResponseEntity.noContent().build();
        }

        // ✅ UPDATE COURSE
        @PutMapping("/course/{courseId}")
        public ResponseEntity<CourseResponseDTO> updateCourse(
                @PathVariable Long courseId,
                @RequestBody CourseRequestDTO dto,
                Principal principal
        ) {
            return ResponseEntity.ok(
                    instructorService.updateCourse(courseId, dto, principal.getName())
            );
        
    }
}
