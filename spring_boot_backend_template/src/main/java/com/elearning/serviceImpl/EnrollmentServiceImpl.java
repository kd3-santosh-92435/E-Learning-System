package com.elearning.serviceImpl;

import com.elearning.entity.Course;
import com.elearning.entity.Enrollment;
import com.elearning.entity.Student;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.service.EnrollmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // =============================
    // ENROLL COURSE
    // =============================
    @Override
    public String enrollCourse(Long studentId, Long courseId) {

        if (enrollmentRepository
                .existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
            return "Student already enrolled in this course";
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        enrollmentRepository.save(
                Enrollment.builder()
                        .student(student)
                        .course(course)
                        .build()
        );

        return "Course enrolled successfully";
    }

    // =============================
    // VALIDATE ENROLLMENT
    // =============================
    @Override
    public void validateEnrollment(Long studentId, Long courseId) {

        boolean enrolled = enrollmentRepository
                .existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId);

        if (!enrolled) {
            throw new RuntimeException("Student is not enrolled in this course");
        }
    }

    // =============================
    // GET LOGGED-IN STUDENT ID
    // =============================
    @Override
    public Long getLoggedInStudentId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String email = authentication.getName();

        return studentRepository.findStudentIdByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
