package com.shannoncode.school.modules.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByProfileIdAndCourseId(String profileId, Long courseId);
}
