package com.elearning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByInstructor_InstructorId(Long instructorId);

    List<Course> findByInstructor_Email(String email);

    Optional<Course> findByCourseIdAndInstructor_Email(Long courseId, String email);
}


