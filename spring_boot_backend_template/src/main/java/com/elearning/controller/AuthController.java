package com.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.dtos.AdminLoginDTO;
import com.elearning.dtos.AdminResponseDTO;
import com.elearning.dtos.JwtResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.StudentRegisterDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.entity.Instructor;
import com.elearning.security.JwtUtil;
import com.elearning.service.AdminService;
import com.elearning.service.InstructorService;
import com.elearning.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;
    private final InstructorService instructorService;
    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    // ================= STUDENT =================

    @PostMapping("/student/register")
    public ResponseEntity<StudentResponseDTO> registerStudent(
            @RequestBody StudentRegisterDTO dto) {

        return ResponseEntity.ok(studentService.register(dto));
    }

    @PostMapping("/student/login")
    public ResponseEntity<JwtResponseDTO> studentLogin(
            @RequestBody LoginRequestDTO dto) {

        StudentResponseDTO student = studentService.login(dto);
        String token = jwtUtil.generateToken(student.getEmail(), "STUDENT");
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ================= INSTRUCTOR =================

    @PostMapping("/instructor/register")
    public ResponseEntity<Instructor> registerInstructor(
            @RequestBody Instructor instructor) {

        return ResponseEntity.ok(instructorService.register(instructor));
    }

    @PostMapping("/instructor/login")
    public ResponseEntity<JwtResponseDTO> instructorLogin(
            @RequestBody LoginRequestDTO dto) {

        Instructor instructor = instructorService.login(dto);
        String token = jwtUtil.generateToken(instructor.getEmail(), "INSTRUCTOR");
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ================= ADMIN =================

    @PostMapping("/admin/login")
    public ResponseEntity<JwtResponseDTO> adminLogin(
            @RequestBody AdminLoginDTO dto) {

        AdminResponseDTO admin = adminService.login(dto);
        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}

