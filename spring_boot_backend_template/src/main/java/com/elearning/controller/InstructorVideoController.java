package com.elearning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.dtos.VideoResponseDTO;
import com.elearning.service.VideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instructor/video")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class InstructorVideoController {

    private final VideoService videoService;

    // ✅ UPLOAD VIDEO (already working)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(
            @RequestParam Long courseId,
            @RequestParam String title,
            @RequestParam MultipartFile file
    ) {
        videoService.uploadVideo(courseId, title, file);
        return ResponseEntity.ok("Video uploaded successfully");
    }

    // ✅ GET VIDEOS BY COURSE
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<VideoResponseDTO>> getVideosByCourse(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                videoService.getVideosByCourse(courseId)
        );
    }
    
    // ✅ DELETE VIDEO
    @DeleteMapping("/{videoId}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long videoId) {
        videoService.deleteVideo(videoId);
        return ResponseEntity.noContent().build();
    }
}
