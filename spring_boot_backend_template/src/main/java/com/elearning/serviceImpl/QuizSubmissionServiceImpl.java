package com.elearning.serviceImpl;

import org.springframework.stereotype.Service;

import com.elearning.dtos.QuizSubmitRequestDTO;
import com.elearning.dtos.QuizSubmitResponseDTO;
import com.elearning.entity.QuizOption;
import com.elearning.repository.QuizOptionRepository;
import com.elearning.service.QuizSubmissionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuizSubmissionServiceImpl implements QuizSubmissionService {

    private final QuizOptionRepository quizOptionRepository;

    @Override
    public QuizSubmitResponseDTO submitQuiz(QuizSubmitRequestDTO dto) {

        int score = 0;
        int totalQuestions = dto.getAnswers().size();

        for (QuizSubmitRequestDTO.AnswerDTO answer : dto.getAnswers()) {

            QuizOption selectedOption = quizOptionRepository
                    .findById(answer.getSelectedOptionId())
                    .orElseThrow(() -> new RuntimeException("Option not found"));

            if (selectedOption.isCorrect()) {
                score++;
            }
        }

        String result = score >= (totalQuestions / 2) ? "PASS" : "FAIL";

        return new QuizSubmitResponseDTO(score, totalQuestions, result);
    }
}
