package com.elearning.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.elearning.dtos.VideoResponseDTO;
import com.elearning.entity.Student;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.repository.VideoRepository;
import com.elearning.service.StudentVideoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentVideoServiceImpl implements StudentVideoService {

    private final VideoRepository videoRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<VideoResponseDTO> getVideosForStudent(Long courseId) {

        // 🔐 Logged-in student
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student not found"));

        // 🔒 CHECK ENROLLMENT
        boolean enrolled = enrollmentRepository
                .existsByStudent_StudentIdAndCourse_CourseId(
                        student.getStudentId(), courseId);

        if (!enrolled) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not enrolled in this course"
            );
        }

        // ✅ RETURN VIDEOS
        return videoRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(v -> VideoResponseDTO.builder()
                        .videoId(v.getVideoId())
                        .title(v.getTitle())
                        .filePath(v.getFilePath())
                        .build())
                .collect(Collectors.toList());
    }
}
