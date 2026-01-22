package com.elearning.service;

import java.util.List;

import com.elearning.dtos.AdminLoginDTO;
import com.elearning.dtos.AdminResponseDTO;
import com.elearning.dtos.InstructorResponseDTO;
import com.elearning.dtos.StudentResponseDTO;

public interface AdminService {

    AdminResponseDTO login(AdminLoginDTO dto);

    List<StudentResponseDTO> getAllStudents();

    List<InstructorResponseDTO> getAllInstructors();

    String deleteStudent(Long studentId);

    String deleteInstructor(Long instructorId);
}

