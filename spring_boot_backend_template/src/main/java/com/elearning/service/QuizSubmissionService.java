package com.elearning.service;

import com.elearning.dtos.QuizSubmitRequestDTO;
import com.elearning.dtos.QuizSubmitResponseDTO;

public interface QuizSubmissionService {

    QuizSubmitResponseDTO submitQuiz(QuizSubmitRequestDTO dto);
}

