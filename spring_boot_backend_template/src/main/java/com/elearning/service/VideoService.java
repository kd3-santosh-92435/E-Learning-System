package com.elearning.service;

import com.elearning.entity.CourseVideo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoService {

    void uploadVideo(Long courseId, MultipartFile file, String title);

    CourseVideo getVideo(Long videoId);

    List<CourseVideo> getVideosByCourse(Long courseId);
}
