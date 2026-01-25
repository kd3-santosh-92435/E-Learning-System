package com.elearning.controller;

import com.elearning.dtos.VideoResponseDTO;
import com.elearning.entity.Video;
import com.elearning.service.EnrollmentService;
import com.elearning.service.StudentVideoService;
import com.elearning.service.VideoService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/student/videos") // ✅ PLURAL (FIXED)
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class StudentVideoController {

    private final VideoService videoService;
    private final StudentVideoService studentVideoService;
    private final EnrollmentService enrollmentService;

    // ===============================
    // LIST VIDEOS OF COURSE
    // ===============================
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<VideoResponseDTO>> getCourseVideos(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                studentVideoService.getVideosForStudent(courseId)
        );
    }


    // ===============================
    // STREAM VIDEO
    // ===============================
//    @GetMapping("/{videoId}")
//    @PreAuthorize("hasRole('STUDENT')")
//    public ResponseEntity<Resource> streamVideo(@PathVariable Long videoId) {
//
//        Video video = videoService.getVideo(videoId);
//
//        Long studentId = enrollmentService.getLoggedInStudentId();
//        enrollmentService.validateEnrollment(
//                studentId,
//                video.getCourse().getCourseId()
//        );
//
//        File file = new File(video.getFilePath());
//
//        if (!file.exists()) {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    "Video file missing on server"
//            );
//        }
//
//        FileSystemResource resource = new FileSystemResource(file);
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
//                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
//                .contentLength(file.length())
//                .body(resource);
//    }

    
    @GetMapping("/{videoId}")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long videoId) {

        Video video = videoService.getVideo(videoId);

        FileSystemResource resource =
                new FileSystemResource(video.getFilePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "video/mp4")
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .body(resource);
    }

}
