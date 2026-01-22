package com.elearning.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseRequestDTO {
    private String title;
    private String description;
    private Double price;
}

