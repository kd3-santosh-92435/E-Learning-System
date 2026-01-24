package com.elearning.service;

import java.util.List;

import com.elearning.dtos.CourseRequestDTO;
import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.QuizRequestDTO;
import com.elearning.dtos.QuizResponseDTO;
import com.elearning.dtos.VideoResponseDTO;
import com.elearning.entity.Instructor;

public interface InstructorService {

    Instructor register(Instructor instructor);

    Instructor login(LoginRequestDTO dto);

    CourseResponseDTO createCourse(String instructorEmail, CourseRequestDTO dto);

    List<CourseResponseDTO> getCoursesByEmail(String email);

    void deleteCourse(Long courseId, String email);

    CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO dto, String email);

    QuizResponseDTO createQuiz(QuizRequestDTO dto);

    List<VideoResponseDTO> getVideosByCourse(Long courseId);
}
