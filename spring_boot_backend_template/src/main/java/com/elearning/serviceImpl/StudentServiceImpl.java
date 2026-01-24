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
import com.elearning.service.EmailService;
import com.elearning.service.StudentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    // ===============================
    // REGISTER
    // ===============================
    @Override
    public StudentResponseDTO register(StudentRegisterDTO dto) {

        if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already registered"
            );
        }

        Student student = Student.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        Student saved = studentRepository.save(student);

        return StudentResponseDTO.builder()
                .studentId(saved.getStudentId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }

    // ===============================
    // LOGIN (NO JWT HERE)
    // ===============================
    @Override
    public StudentResponseDTO login(LoginRequestDTO dto) {

        Student student = studentRepository.findByEmail(dto.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Invalid email or password"
                        )
                );

        if (!passwordEncoder.matches(dto.getPassword(), student.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        return StudentResponseDTO.builder()
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
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

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Already enrolled in this course"
            );
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Course not found"
                        )
                );

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .build();

        enrollmentRepository.save(enrollment);

        // ✅ SEND EMAIL
        emailService.sendCourseEnrollmentEmail(
                student.getEmail(),
                student.getName(),
                course.getTitle()
        );

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
    // UPDATE PROFILE
    // ===============================
    @Override
    public void updateProfile(UpdateProfileDTO dto) {

        Student student = getLoggedInStudent();

        // Update name
        if (dto.getName() != null && !dto.getName().isBlank()) {
            student.setName(dto.getName());
        }

        // Update email
        if (dto.getEmail() != null &&
            !dto.getEmail().equals(student.getEmail())) {

            if (studentRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Email already in use"
                );
            }
            student.setEmail(dto.getEmail());
        }

        // Change password
        if (dto.getNewPassword() != null && !dto.getNewPassword().isBlank()) {

            if (dto.getOldPassword() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Old password is required"
                );
            }

            if (!passwordEncoder.matches(
                    dto.getOldPassword(),
                    student.getPassword())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Old password is incorrect"
                );
            }

            student.setPassword(
                    passwordEncoder.encode(dto.getNewPassword())
            );
        }

        studentRepository.save(student);
    }

    // ===============================
    // UTILITY: GET LOGGED-IN STUDENT
    // ===============================
    private Student getLoggedInStudent() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Student not found"
                        )
                );
    }
    
    @Override
    public Long getLoggedInStudentId() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return studentRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Student not found"
                        ))
                .getStudentId();
    }

}
