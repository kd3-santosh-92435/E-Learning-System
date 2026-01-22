package com.elearning.service;

import java.util.List;

import com.elearning.dtos.CourseRequestDTO;
import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.QuizRequestDTO;
import com.elearning.dtos.QuizResponseDTO;
import com.elearning.entity.Instructor;

public interface InstructorService {

    Instructor register(Instructor instructor);

    Instructor login(LoginRequestDTO dto);

    CourseResponseDTO createCourse(Long instructorId, CourseRequestDTO dto);

    QuizResponseDTO createQuiz(QuizRequestDTO dto);

    List<CourseResponseDTO> getCourses(Long instructorId);
}

