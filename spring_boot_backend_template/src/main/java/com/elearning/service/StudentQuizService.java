package com.elearning.service;





import java.util.List;

import com.elearning.dtos.QuizQuestionWithOptionsDTO;

public interface StudentQuizService {

    List<QuizQuestionWithOptionsDTO> getQuizQuestions(Long quizId);
}

