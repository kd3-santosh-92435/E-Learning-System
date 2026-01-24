package com.elearning.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.elearning.dtos.CourseRequestDTO;
import com.elearning.dtos.CourseResponseDTO;
import com.elearning.dtos.LoginRequestDTO;
import com.elearning.dtos.QuizRequestDTO;
import com.elearning.dtos.QuizResponseDTO;
import com.elearning.dtos.VideoResponseDTO;
import com.elearning.entity.Course;
import com.elearning.entity.Instructor;
import com.elearning.entity.Quiz;
import com.elearning.repository.CourseRepository;
import com.elearning.repository.InstructorRepository;
import com.elearning.repository.QuizRepository;
import com.elearning.repository.VideoRepository;
import com.elearning.service.InstructorService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final QuizRepository quizRepository;
    private final VideoRepository videoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Instructor register(Instructor instructor) {
        instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor login(LoginRequestDTO dto) {

        Instructor instructor = instructorRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(dto.getPassword(), instructor.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return instructor;
    }

    // ================= COURSE =================

    @Override
    public CourseResponseDTO createCourse(String instructorEmail, CourseRequestDTO dto) {

        Instructor instructor = instructorRepository.findByEmail(instructorEmail)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Instructor not found"));

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
    public List<CourseResponseDTO> getCoursesByEmail(String email) {

        Instructor instructor = instructorRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Instructor not found"));

        return courseRepository.findByInstructor_InstructorId(instructor.getInstructorId())
                .stream()
                .map(c -> CourseResponseDTO.builder()
                        .courseId(c.getCourseId())
                        .title(c.getTitle())
                        .description(c.getDescription())
                        .price(c.getPrice())
                        .instructorName(instructor.getName())
                        .build())
                .collect(Collectors.toList());
    }

    // ✅ DELETE COURSE (FIXED)
    @Override
    public void deleteCourse(Long courseId, String email) {

        Course course = courseRepository
                .findByCourseIdAndInstructor_Email(courseId, email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found or not yours"));

        courseRepository.delete(course);
    }

    // ✅ UPDATE COURSE (FIXED)
    @Override
    public CourseResponseDTO updateCourse(
            Long courseId,
            CourseRequestDTO dto,
            String email
    ) {

        Course course = courseRepository
                .findByCourseIdAndInstructor_Email(courseId, email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found or not yours"));

        course.setTitle(dto.getTitle());
        course.setDescription(dto.getDescription());
        course.setPrice(dto.getPrice());

        Course saved = courseRepository.save(course);

        return CourseResponseDTO.builder()
                .courseId(saved.getCourseId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .price(saved.getPrice())
                .instructorName(saved.getInstructor().getName())
                .build();
    }

    // ================= QUIZ =================

    @Override
    public QuizResponseDTO createQuiz(QuizRequestDTO dto) {

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Course not found"));

        Quiz quiz = Quiz.builder()
                .title(dto.getTitle())
                .course(course)
                .build();

        Quiz saved = quizRepository.save(quiz);

        return new QuizResponseDTO(saved.getQuizId(), saved.getTitle());
    }

    // ================= VIDEO =================

    @Override
    public List<VideoResponseDTO> getVideosByCourse(Long courseId) {

        return videoRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(video -> VideoResponseDTO.builder()
                        .videoId(video.getVideoId())
                        .title(video.getTitle())
                        .filePath(video.getFilePath())
                        .build())
                .collect(Collectors.toList());
    }
}

    

