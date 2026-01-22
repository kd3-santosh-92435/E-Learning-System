package com.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.dtos.QuizSubmitRequestDTO;
import com.elearning.dtos.QuizSubmitResponseDTO;
import com.elearning.service.QuizSubmissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student/quiz")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentQuizSubmissionController {

    private final QuizSubmissionService quizSubmissionService;

    @PostMapping("/submit")
    public ResponseEntity<QuizSubmitResponseDTO> submitQuiz(
            @RequestBody QuizSubmitRequestDTO dto) {

        return ResponseEntity.ok(
                quizSubmissionService.submitQuiz(dto)
        );
    }
}
