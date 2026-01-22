package com.elearning.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuizSubmitResponseDTO {

    private int score;
    private int totalQuestions;
    private String result;
}

