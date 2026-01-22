package com.elearning.serviceImpl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.elearning.dtos.CourseRequestDTO;
import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.QuizRequestDTO;
import com.elearning.dtos.QuizResponseDTO;
import com.elearning.entity.Course;
import com.elearning.entity.Instructor;
import com.elearning.entity.Quiz;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.InstructorRepository;
import com.elearning.repository.QuizRepository;
import com.elearning.service.InstructorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final QuizRepository quizRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Instructor register(Instructor instructor) {
        instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor login(LoginRequestDTO dto) {
        Instructor instructor = instructorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        if (!passwordEncoder.matches(dto.getPassword(), instructor.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        return instructor;
    }

    @Override
    public CourseResponseDTO createCourse(Long instructorId, CourseRequestDTO dto) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = Course.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .instructor(instructor)
                .build();

        Course saved = courseRepository.save(course);

        return CourseResponseDTO.builder()
                .courseId(saved.getCourseId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .price(saved.getPrice())
                .instructorName(instructor.getName())
                .build();
    }

    @Override
    public QuizResponseDTO createQuiz(QuizRequestDTO dto) {
        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Quiz quiz = Quiz.builder()
                .title(dto.getTitle())
                .course(course)
                .build();

        Quiz saved = quizRepository.save(quiz);

        return new QuizResponseDTO(saved.getQuizId(), saved.getTitle());
    }

    @Override
    public List<CourseResponseDTO> getCourses(Long instructorId) {
        return courseRepository.findByInstructor_InstructorId(instructorId)
                .stream()
                .map(c -> CourseResponseDTO.builder()
                        .courseId(c.getCourseId())
                        .title(c.getTitle())
                        .description(c.getDescription())
                        .price(c.getPrice())
                        .instructorName(c.getInstructor().getName())
                        .build())
                .toList();
    }
}

