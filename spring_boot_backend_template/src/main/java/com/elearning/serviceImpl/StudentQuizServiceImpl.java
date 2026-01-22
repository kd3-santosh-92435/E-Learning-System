package com.elearning.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.elearning.dtos.QuizQuestionWithOptionsDTO;
import com.elearning.entity.QuizQuestion;
import com.elearning.repository.QuizOptionRepository;
import com.elearning.repository.QuizQuestionRepository;
import com.elearning.service.StudentQuizService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentQuizServiceImpl implements StudentQuizService {

    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizOptionRepository quizOptionRepository;

    @Override
    public List<QuizQuestionWithOptionsDTO> getQuizQuestions(Long quizId) {

        List<QuizQuestion> questions =
                quizQuestionRepository.findByQuiz_QuizId(quizId);

        return questions.stream().map(question ->
                new QuizQuestionWithOptionsDTO(
                        question.getQuestionId(),
                        question.getQuestionText(),
                        quizOptionRepository
                                .findByQuestion_QuestionId(question.getQuestionId())
                                .stream()
                                .map(option ->
                                        new QuizQuestionWithOptionsDTO.OptionDTO(
                                                option.getOptionId(),
                                                option.getOptionText()
                                        )
                                )
                                .toList()
                )
        ).toList();
    }
}
