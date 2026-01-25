package com.elearning.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.elearning.dtos.VideoResponseDTO;
import com.elearning.service.VideoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/instructor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
public class VideoController {

    private final VideoService videoService;

    @PostMapping(
    	    value = "/course/{courseId}/video",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    	)
    	public ResponseEntity<VideoResponseDTO> uploadVideo(
    	        @PathVariable Long courseId,
    	        @RequestParam("title") String title,
    	        @RequestParam("file") MultipartFile file
    	) {
    	    return ResponseEntity.ok(
    	            videoService.uploadVideo(courseId, title, file)
    	    );
    	}


    @GetMapping("/course/{courseId}/videos")
    public ResponseEntity<List<VideoResponseDTO>> getVideos(
            @PathVariable Long courseId
    ) {
        return ResponseEntity.ok(
                videoService.getVideosByCourse(courseId)
        );
    }
}
