package com.elearning.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class QuizRequestDTO {
    @NotBlank(message = "Quiz title is required")
    private String title;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}

