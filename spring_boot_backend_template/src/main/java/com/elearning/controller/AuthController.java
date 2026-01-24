package com.elearning.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.elearning.dtos.*;
import com.elearning.entity.Instructor;
import com.elearning.security.JwtUtil;
import com.elearning.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final StudentService studentService;
    private final InstructorService instructorService;
    private final AdminService adminService;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    // ---------- STUDENT ----------

//    @PostMapping("/student/register")
//    public ResponseEntity<StudentResponseDTO> registerStudent(
//            @Valid @RequestBody StudentRegisterDTO dto) {
//        return ResponseEntity.ok(studentService.register(dto));
//    }
    
    @PostMapping("/student/register")
    public ResponseEntity<StudentResponseDTO> registerStudent(
            @RequestBody StudentRegisterDTO dto) {

        System.out.println("🔥 STUDENT REGISTER HIT 🔥");
        return ResponseEntity.ok(studentService.register(dto));
    }


    @PostMapping("/student/login")
    public ResponseEntity<JwtResponseDTO> studentLogin(
            @Valid @RequestBody LoginRequestDTO dto) {

        StudentResponseDTO student = studentService.login(dto);
        String token = jwtUtil.generateToken(student.getEmail(), "STUDENT");

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ---------- INSTRUCTOR ----------

    @PostMapping("/instructor/register")
    public ResponseEntity<Instructor> registerInstructor(
            @Valid @RequestBody Instructor instructor) {
        return ResponseEntity.ok(instructorService.register(instructor));
    }

    @PostMapping("/instructor/login")
    public ResponseEntity<JwtResponseDTO> instructorLogin(
            @Valid @RequestBody LoginRequestDTO dto) {

        Instructor instructor = instructorService.login(dto);

        String token = jwtUtil.generateToken(
                instructor.getEmail(),
                "INSTRUCTOR",
                instructor.getInstructorId()
        );

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ---------- ADMIN ----------

    @PostMapping("/admin/login")
    public ResponseEntity<JwtResponseDTO> adminLogin(
            @Valid @RequestBody AdminLoginDTO dto) {

        AdminResponseDTO admin = adminService.login(dto);
        String token = jwtUtil.generateToken(admin.getEmail(), "ADMIN");

        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    // ---------- PASSWORD ----------

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordDTO dto) {
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
