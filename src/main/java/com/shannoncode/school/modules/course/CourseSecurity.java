package com.shannoncode.school.modules.course;

import com.shannoncode.school.modules.enrollment.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseSecurity {

    private final EnrollmentRepository enrollmentRepository;

    public boolean isEnrolled(String profileId, Long courseId) {
        return enrollmentRepository.existsByProfileIdAndCourseId(profileId, courseId);
    }
}
