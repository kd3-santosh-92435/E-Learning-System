package com.elearning.service;

public interface EmailService {

    void sendCourseEnrollmentEmail(
            String to,
            String studentName,
            String courseName
    );
    
    void sendPasswordResetEmail(String to, String resetLink);

}
