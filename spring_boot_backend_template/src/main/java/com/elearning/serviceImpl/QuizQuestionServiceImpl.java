package com.elearning.serviceImpl;

import org.springframework.stereotype.Service;

import com.elearning.dtos.QuizQuestionRequestDTO;
import com.elearning.dtos.QuizQuestionResponseDTO;
import com.elearning.entity.Quiz;
import com.elearning.entity.QuizQuestion;
import com.elearning.repository.QuizQuestionRepository;
import com.elearning.repository.QuizRepository;
import com.elearning.service.QuizQuestionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;

    @Override
    public QuizQuestionResponseDTO addQuestion(QuizQuestionRequestDTO dto) {

        Quiz quiz = quizRepository.findById(dto.getQuizId())
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        QuizQuestion question = QuizQuestion.builder()
                .quiz(quiz)
                .questionText(dto.getQuestionText())
                .build();

        QuizQuestion saved = quizQuestionRepository.save(question);

        return new QuizQuestionResponseDTO(
                saved.getQuestionId(),
                saved.getQuestionText()
        );
    }

}
