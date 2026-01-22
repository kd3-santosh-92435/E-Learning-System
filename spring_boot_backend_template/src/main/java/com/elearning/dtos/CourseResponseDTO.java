package com.elearning.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CourseResponseDTO {
    private Long courseId;
    private String title;
    private String description;
    private Double price;
    private String instructorName;
}

