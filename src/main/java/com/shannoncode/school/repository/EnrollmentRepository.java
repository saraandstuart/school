package com.shannoncode.school.repository;

import com.shannoncode.school.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByProfileIdAndCourseId(String profileId, Long courseId);
}
