package com.elearning.serviceImpl;

import com.elearning.dtos.ForgotPasswordDTO;
import com.elearning.dtos.ResetPasswordDTO;
import com.elearning.entity.PasswordResetToken;
import com.elearning.entity.Student;
import com.elearning.repository.PasswordResetTokenRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.service.AuthService;
import com.elearning.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final StudentRepository studentRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ===============================
    // FORGOT PASSWORD
    // ===============================
    @Override
    public void forgotPassword(ForgotPasswordDTO dto) {

        Student student = studentRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .student(student)
                .expiryTime(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(resetToken);

        String resetLink =
                "http://localhost:3000/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(
                student.getEmail(),
                resetLink
        );
    }

    // ===============================
    // RESET PASSWORD
    // ===============================
    @Override
    public void resetPassword(ResetPasswordDTO dto) {

        PasswordResetToken resetToken = tokenRepository.findByToken(dto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        Student student = resetToken.getStudent();
        student.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        studentRepository.save(student);
        tokenRepository.delete(resetToken);
    }
}
