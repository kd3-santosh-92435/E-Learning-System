package com.elearning.service;

import java.util.List;
import com.elearning.dtos.VideoResponseDTO;

public interface StudentVideoService {

    List<VideoResponseDTO> getVideosForStudent(Long courseId);
}
