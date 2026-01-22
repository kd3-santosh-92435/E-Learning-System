package com.elearning.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class QuizResponseDTO {
    private Long quizId;
    private String title;
}

