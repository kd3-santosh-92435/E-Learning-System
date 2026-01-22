package com.elearning.controller;

import com.elearning.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student/enrollment")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollCourse(
            @RequestParam Long courseId) {

        Long studentId = enrollmentService.getLoggedInStudentId();

        String response = enrollmentService.enrollCourse(studentId, courseId);
        return ResponseEntity.ok(response);
    }
}
