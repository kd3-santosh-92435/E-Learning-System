package com.elearning.serviceImpl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.StudentRegisterDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.entity.Course;
import com.elearning.entity.Enrollment;
import com.elearning.entity.Student;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.security.JwtUtil;
import com.elearning.service.StudentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // =============================
    // REGISTER
    // =============================
    @Override
    public StudentResponseDTO register(StudentRegisterDTO dto) {

        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        studentRepository.save(student);

        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .build();
    }

    // =============================
    // LOGIN
    // =============================
    @Override
    public StudentResponseDTO login(LoginRequestDTO dto) {

        Student student = studentRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), student.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(student.getEmail(), "STUDENT");

        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .token(token)
                .build();
    }

    // =============================
    // PROFILE
    // =============================
    @Override
    public StudentResponseDTO getProfile() {

        Student student = getLoggedInStudent();

        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .build();
    }

    // =============================
    // ALL COURSES
    // =============================
    @Override
    public List<CourseResponseDTO> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(course -> CourseResponseDTO.builder()
                        .courseId(course.getCourseId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .price(course.getPrice())
                        .instructorName(course.getInstructor().getName())
                        .build())
                .toList();
    }

    // =============================
    // ENROLL COURSE (JWT BASED)
    // =============================
    @Override
    public String enrollCourse(Long courseId) {

        Student student = getLoggedInStudent();

        if (enrollmentRepository
                .existsByStudent_StudentIdAndCourse_CourseId(
                        student.getStudentId(), courseId)) {
            return "Student already enrolled in this course";
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);

        return "Course enrolled successfully";
    }

    // =============================
    // MY COURSES
    // =============================
    @Override
    public List<CourseResponseDTO> getMyCourses() {

        Student student = getLoggedInStudent();

        return enrollmentRepository.findByStudent_StudentId(student.getStudentId())
                .stream()
                .map(enrollment -> {
                    Course course = enrollment.getCourse();
                    return CourseResponseDTO.builder()
                            .courseId(course.getCourseId())
                            .title(course.getTitle())
                            .description(course.getDescription())
                            .price(course.getPrice())
                            .instructorName(course.getInstructor().getName())
                            .build();
                })
                .toList();
    }

    // =============================
    // PRIVATE HELPER
    // =============================
    private Student getLoggedInStudent() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }

        String email = authentication.getName();

        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
}
