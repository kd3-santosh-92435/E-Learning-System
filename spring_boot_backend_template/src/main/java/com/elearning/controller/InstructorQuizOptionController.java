package com.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.dtos.QuizOptionRequestDTO;
import com.elearning.service.QuizOptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instructor/quiz/question")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorQuizOptionController {

    private final QuizOptionService quizOptionService;

    @PostMapping("/option")
    public ResponseEntity<String> addOptions(
            @RequestBody QuizOptionRequestDTO dto) {

        return ResponseEntity.ok(quizOptionService.addOptions(dto));
    }
}

