package com.elearning.dtos;



import lombok.Data;
import java.util.List;

@Data
public class QuizSubmitRequestDTO {

    private Long studentId;
    private Long quizId;
    private List<AnswerDTO> answers;

    @Data
    public static class AnswerDTO {
        private Long questionId;
        private Long selectedOptionId;
    }
}

