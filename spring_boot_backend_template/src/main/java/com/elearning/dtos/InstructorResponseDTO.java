package com.elearning.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class InstructorResponseDTO {
    private Long instructorId;
    private String name;
    private String email;
}

