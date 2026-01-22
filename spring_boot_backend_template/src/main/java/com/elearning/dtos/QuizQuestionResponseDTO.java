package com.elearning.dtos;

import lombok.*;

@Data
@AllArgsConstructor
public class QuizQuestionResponseDTO {
    private Long questionId;
    private String questionText;
}

