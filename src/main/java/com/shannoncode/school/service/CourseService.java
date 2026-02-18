package com.shannoncode.school.service;

import com.shannoncode.school.model.Course;
import com.shannoncode.school.repository.CourseRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isEnrolled(principal.username, #courseId)")
    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }
}
