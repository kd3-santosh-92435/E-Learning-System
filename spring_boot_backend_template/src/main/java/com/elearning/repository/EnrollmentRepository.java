package com.elearning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.elearning.entity.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudent_StudentIdAndCourse_CourseId(
            Long studentId,
            Long courseId
    );

    List<Enrollment> findByStudent_StudentId(Long studentId);
}
