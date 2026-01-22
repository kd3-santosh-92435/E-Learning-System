package com.elearning.service;

import java.util.List;

import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.StudentRegisterDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.dtos.UpdateProfileDTO;

public interface StudentService {

    // AUTH
    StudentResponseDTO register(StudentRegisterDTO dto);
    StudentResponseDTO login(LoginRequestDTO dto);

    // PROFILE
    StudentResponseDTO getProfile();

    // COURSES
    List<CourseResponseDTO> getAllCourses();
    String enrollCourse(Long courseId);
    List<CourseResponseDTO> getMyCourses();
    
    void updateProfile(UpdateProfileDTO dto);

}
