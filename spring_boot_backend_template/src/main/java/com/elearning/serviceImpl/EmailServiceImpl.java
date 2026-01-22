package com.elearning.serviceImpl;

import com.elearning.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendCourseEnrollmentEmail(
            String to,
            String studentName,
            String courseName) {

        System.out.println("📧 Sending email to: " + to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Course Enrollment Successful");
        message.setText(
                "Hello " + studentName + ",\n\n" +
                "You have successfully enrolled in the course:\n" +
                courseName + "\n\n" +
                "Happy Learning!\n\nE-Learning Team"
        );

        mailSender.send(message);

        System.out.println("✅ Email send() method executed");
    }
    
    @Override
    public void sendPasswordResetEmail(String to, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText(
                "Click the link below to reset your password:\n\n" +
                resetLink + "\n\n" +
                "This link is valid for 15 minutes.\n\n" +
                "If you did not request this, please ignore."
        );

        mailSender.send(message);
    }


}
