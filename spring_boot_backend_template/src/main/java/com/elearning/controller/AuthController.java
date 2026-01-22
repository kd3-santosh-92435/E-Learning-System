package com.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elearning.dtos.AdminLoginDTO;
import com.elearning.dtos.AdminResponseDTO;
import com.elearning.dtos.ForgotPasswordDTO;
import com.elearning.dtos.JwtResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.ResetPasswordDTO;
import com.elearning.dtos.StudentRegisterDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.entity.Instructor;
import com.elearning.security.JwtUtil;
import com.elearning.service.AdminService;
import com.elearning.service.AuthService;
import com.elearning.service.InstructorService;
import com.elearning.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;
    private final InstructorService instructorService;
    private final AdminService adminService;
    private final JwtUtil jwtUtil;

    private final AuthService authService;

    // ================= STUDENT =================

    @PostMapping("/student/register")
    public ResponseEntity<StudentResponseDTO> registerStudent(
            @Valid @RequestBody StudentRegisterDTO dto) {

        return ResponseEntity.ok(studentService.register(dto));
    }

    @PostMapping("/student/login")
    public ResponseEntity<JwtResponseDTO> studentLogin(
           @Valid @RequestBody LoginRequestDTO dto) {

        StudentResponseDTO student = studentService.login(dto);
        String token = jwtUtil.generateToken(student.getEmail(), "STUDENT");
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ================= INSTRUCTOR =================

    @PostMapping("/instructor/register")
    public ResponseEntity<Instructor> registerInstructor(
           @Valid @RequestBody Instructor instructor) {

        return ResponseEntity.ok(instructorService.register(instructor));
    }

    @PostMapping("/instructor/login")
    public ResponseEntity<JwtResponseDTO> instructorLogin(
          @Valid  @RequestBody LoginRequestDTO dto) {

        Instructor instructor = instructorService.login(dto);
        String token = jwtUtil.generateToken(instructor.getEmail(), "INSTRUCTOR");
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ================= ADMIN =================

    @PostMapping("/admin/login")
    public ResponseEntity<JwtResponseDTO> adminLogin(
          @Valid  @RequestBody AdminLoginDTO dto) {

        AdminResponseDTO admin = adminService.login(dto);
        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
    
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
          @Valid  @RequestBody ForgotPasswordDTO dto) {

        authService.forgotPassword(dto);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
           @Valid @RequestBody ResetPasswordDTO dto) {

        authService.resetPassword(dto);
        return ResponseEntity.ok("Password updated successfully");
    }
    
}

