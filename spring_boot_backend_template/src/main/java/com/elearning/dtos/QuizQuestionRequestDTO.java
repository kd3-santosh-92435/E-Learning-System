package com.elearning.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class QuizQuestionRequestDTO {
    private Long quizId;
    private String questionText;
}

