package com.elearning.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AdminResponseDTO {
    private Long adminId;
    private String name;
    private String email;
}

