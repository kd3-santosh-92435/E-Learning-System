package com.elearning.dtos;

import java.util.List;

import lombok.Data;

@Data
public class QuizOptionRequestDTO {
    private Long questionId;
    private List<OptionDTO> options;

    @Data
    public static class OptionDTO {
        private String text;
        private boolean correct;
    }
}

