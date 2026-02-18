package com.shannoncode.school.component;

import com.shannoncode.school.repository.EnrollmentRepository;
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
