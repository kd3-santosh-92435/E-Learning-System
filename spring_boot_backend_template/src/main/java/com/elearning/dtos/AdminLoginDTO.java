package com.elearning.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminLoginDTO {
    private String email;
    private String password;
}

