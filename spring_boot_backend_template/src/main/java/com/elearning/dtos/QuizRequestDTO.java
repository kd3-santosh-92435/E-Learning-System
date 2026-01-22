package com.elearning.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuizRequestDTO {
    private String title;
    private Long courseId;
}

