package com.elearning.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.elearning.dtos.VideoResponseDTO;
import com.elearning.entity.Video;

public interface VideoService {

    VideoResponseDTO uploadVideo(Long courseId, String title, MultipartFile file);

    List<VideoResponseDTO> getVideosByCourse(Long courseId);

	Video getVideo(Long videoId);
	void deleteVideo(Long videoId);
}
