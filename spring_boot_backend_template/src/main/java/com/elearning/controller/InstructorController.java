package com.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/course")
    public ResponseEntity<CourseResponseDTO> createCourse(
            @RequestParam Long instructorId,
            @RequestBody CourseRequestDTO dto) {

        return ResponseEntity.ok(
                instructorService.createCourse(instructorId, dto)
        );
    }

    @PostMapping("/quiz")
    public ResponseEntity<QuizResponseDTO> createQuiz(
            @RequestBody QuizRequestDTO dto) {

        return ResponseEntity.ok(
                instructorService.createQuiz(dto)
        );
    }

    @GetMapping("/courses/{instructorId}")
    public ResponseEntity<List<CourseResponseDTO>> getCourses(
            @PathVariable Long instructorId) {

        return ResponseEntity.ok(
                instructorService.getCourses(instructorId)
        );
    }
}

