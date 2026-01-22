package com.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.dtos.QuizQuestionWithOptionsDTO;
import com.elearning.service.StudentQuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student/quiz")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentQuizController {

    private final StudentQuizService studentQuizService;

    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<QuizQuestionWithOptionsDTO>> getQuizQuestions(
            @PathVariable Long quizId) {

        return ResponseEntity.ok(
                studentQuizService.getQuizQuestions(quizId)
        );
    }
}
