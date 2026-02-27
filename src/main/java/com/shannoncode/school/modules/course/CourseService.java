package com.shannoncode.school.modules.course;

import com.shannoncode.school.common.exception.ResourceNotFoundException;
import com.shannoncode.school.modules.course.dto.CourseRequest;
import com.shannoncode.school.modules.course.dto.CourseResponse;
import com.shannoncode.school.common.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;
    private final CourseMapper mapper;

    @Transactional
    public CourseResponse create(CourseRequest courseRequest) {
        Course course = mapper.toEntity(courseRequest);
        Course savedCourse = repository.save(course);
        return mapper.toResponse(savedCourse);
    }

    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isEnrolled(principal.username, #id)")
    public CourseResponse findById(Long id) {
        return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return repository.findAll(pageable)
            .map(mapper::toResponse);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
