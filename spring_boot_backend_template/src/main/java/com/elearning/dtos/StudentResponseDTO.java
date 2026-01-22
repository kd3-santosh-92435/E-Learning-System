package com.elearning.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StudentResponseDTO {
    private Long studentId;
    private String name;
    private String email;
    
    private String token;
}
