package com.elearning.controller;

import com.elearning.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping(
        value = "/video/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> uploadVideo(
            @RequestParam("courseId") Long courseId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) {

        videoService.uploadVideo(courseId, file, title);
        return ResponseEntity.ok("Video uploaded successfully");
    }
}
