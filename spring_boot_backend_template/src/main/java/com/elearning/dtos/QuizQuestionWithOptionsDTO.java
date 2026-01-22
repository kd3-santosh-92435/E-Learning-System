package com.elearning.dtos;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class QuizQuestionWithOptionsDTO {

    private Long questionId;
    private String questionText;
    private List<OptionDTO> options;

    @Data
    @AllArgsConstructor
    public static class OptionDTO {
        private Long optionId;
        private String text;
    }
}

