package com.elearning.serviceImpl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearning.dtos.AdminLoginDTO;
import com.elearning.dtos.AdminResponseDTO;
import com.elearning.dtos.InstructorResponseDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.entity.Admin;
import com.elearning.repository.AdminRepository;
import com.elearning.repository.InstructorRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminResponseDTO login(AdminLoginDTO dto) {
        Admin admin = adminRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return AdminResponseDTO.builder()
                .adminId(admin.getAdminId())
                .name(admin.getName())
                .email(admin.getEmail())
                .build();
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> StudentResponseDTO.builder()
                        .studentId(s.getStudentId())
                        .name(s.getName())
                        .email(s.getEmail())
                        .build())
                .toList();
    }

    @Override
    public List<InstructorResponseDTO> getAllInstructors() {
        return instructorRepository.findAll().stream()
                .map(i -> InstructorResponseDTO.builder()
                        .instructorId(i.getInstructorId())
                        .name(i.getName())
                        .email(i.getEmail())
                        .build())
                .toList();
    }

    @Override
    public String deleteStudent(Long studentId) {
        studentRepository.deleteById(studentId);
        return "Student deleted successfully";
    }

    @Override
    public String deleteInstructor(Long instructorId) {
        instructorRepository.deleteById(instructorId);
        return "Instructor deleted successfully";
    }
}

