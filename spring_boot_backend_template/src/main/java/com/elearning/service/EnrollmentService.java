package com.elearning.service;

public interface EnrollmentService {

    String enrollCourse(Long studentId, Long courseId);

    void validateEnrollment(Long studentId, Long courseId);

    Long getLoggedInStudentId();
}
