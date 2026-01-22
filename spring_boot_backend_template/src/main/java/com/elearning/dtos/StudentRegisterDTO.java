package com.elearning.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentRegisterDTO {
    private String name;
    private String email;
    private String password;
}

