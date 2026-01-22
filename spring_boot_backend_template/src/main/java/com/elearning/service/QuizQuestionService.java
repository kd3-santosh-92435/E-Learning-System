package com.elearning.service;

import com.elearning.dtos.QuizQuestionRequestDTO;
import com.elearning.dtos.QuizQuestionResponseDTO;
import com.elearning.entity.QuizQuestion;

public interface QuizQuestionService {
	QuizQuestionResponseDTO addQuestion(QuizQuestionRequestDTO dto);
}

