package com.elearning.controller;

import com.elearning.entity.CourseVideo;
import com.elearning.service.EnrollmentService;
import com.elearning.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/video")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentVideoController {

    private final VideoService videoService;
    private final EnrollmentService enrollmentService;

    @GetMapping("/{videoId}")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long videoId) {

        CourseVideo video = videoService.getVideo(videoId);

        Long studentId = enrollmentService.getLoggedInStudentId();

        enrollmentService.validateEnrollment(
                studentId,
                video.getCourse().getCourseId()
        );

        FileSystemResource resource = new FileSystemResource(video.getFilePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(resource);
    }
}
