package com.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.dtos.QuizQuestionRequestDTO;
import com.elearning.dtos.QuizQuestionResponseDTO;
import com.elearning.service.QuizQuestionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instructor/quiz")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorQuizController {

    private final QuizQuestionService quizQuestionService;

    @PostMapping("/question")
    public ResponseEntity<QuizQuestionResponseDTO> addQuestion(
            @RequestBody QuizQuestionRequestDTO dto) {

        return ResponseEntity.ok(quizQuestionService.addQuestion(dto));
    }

}

