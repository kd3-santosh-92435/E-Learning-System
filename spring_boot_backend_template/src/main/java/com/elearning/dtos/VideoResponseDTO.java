package com.elearning.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoResponseDTO {
    private Long videoId;
    private String title;
    private String filePath;
}
