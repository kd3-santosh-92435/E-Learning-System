package com.elearning.controller;

import com.elearning.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/instructor/video")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorVideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam MultipartFile file) {

        videoService.uploadVideo(courseId, file, title);
        return ResponseEntity.ok("Video uploaded successfully");
    }
}
