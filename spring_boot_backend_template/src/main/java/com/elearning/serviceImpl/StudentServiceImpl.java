package com.elearning.serviceImpl;

import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.StudentRegisterDTO;
import com.elearning.dtos.StudentResponseDTO;
import com.elearning.dtos.UpdateProfileDTO;
import com.elearning.entity.Course;
import com.elearning.entity.Enrollment;
import com.elearning.entity.Student;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.EnrollmentRepository;
import com.elearning.repository.StudentRepository;
import com.elearning.security.JwtUtil;
import com.elearning.service.EmailService;
import com.elearning.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    // ===============================
    // REGISTER
    // ===============================
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

    // ===============================
    // LOGIN
    // ===============================
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

    // ===============================
    // GET PROFILE (JWT BASED)
    // ===============================
    @Override
    public StudentResponseDTO getProfile() {

        Student student = getLoggedInStudent();

        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .build();
    }

    // ===============================
    // GET ALL COURSES
    // ===============================
    @Override
    public List<CourseResponseDTO> getAllCourses() {

        return courseRepository.findAll()
                .stream()
                .map(course -> CourseResponseDTO.builder()
                        .courseId(course.getCourseId())
                        .title(course.getTitle())
                        .description(course.getDescription())
                        .price(course.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    // ===============================
    // ENROLL COURSE + EMAIL
    // ===============================
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

        // ✅ SEND COURSE ENROLLMENT EMAIL
        try {
            emailService.sendCourseEnrollmentEmail(
                    student.getEmail(),
                    student.getName(),
                    course.getTitle()
            );
        } catch (Exception e) {
            // Email failure should NOT block enrollment
            e.printStackTrace();
        }

        return "Course enrolled successfully";
    }

    // ===============================
    // GET MY COURSES
    // ===============================
    @Override
    public List<CourseResponseDTO> getMyCourses() {

        Student student = getLoggedInStudent();

        return enrollmentRepository
                .findByStudent_StudentId(student.getStudentId())
                .stream()
                .map(enrollment -> {
                    Course course = enrollment.getCourse();
                    return CourseResponseDTO.builder()
                            .courseId(course.getCourseId())
                            .title(course.getTitle())
                            .description(course.getDescription())
                            .price(course.getPrice())
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ===============================
    // UTILITY: GET LOGGED IN STUDENT
    // ===============================
    private Student getLoggedInStudent() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }
    
    @Override
    public void updateProfile(UpdateProfileDTO dto) {

        Student student = getLoggedInStudent();

        // ===============================
        // UPDATE NAME
        // ===============================
        if (dto.getName() != null && !dto.getName().isBlank()) {
            student.setName(dto.getName());
        }

        // ===============================
        // UPDATE EMAIL
        // ===============================
        if (dto.getEmail() != null &&
            !dto.getEmail().equals(student.getEmail())) {

            if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new RuntimeException("Email already in use");
            }
            student.setEmail(dto.getEmail());
        }

        // ===============================
        // CHANGE PASSWORD (OPTIONAL)
        // ===============================
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {

            if (dto.getOldPassword() == null) {
                throw new RuntimeException("Old password is required");
            }

            if (!passwordEncoder.matches(
                    dto.getOldPassword(),
                    student.getPassword())) {
                throw new RuntimeException("Old password is incorrect");
            }

            student.setPassword(
                    passwordEncoder.encode(dto.getNewPassword())
            );
        }

        studentRepository.save(student);
    }

}
