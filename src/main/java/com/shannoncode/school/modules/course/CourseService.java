package com.shannoncode.school.modules.course;

import com.shannoncode.school.common.exception.ResourceNotFoundException;
import com.shannoncode.school.common.mapper.CourseMapper;
import com.shannoncode.school.modules.course.dto.CoursePatchRequest;
import com.shannoncode.school.modules.course.dto.CourseRequest;
import com.shannoncode.school.modules.course.dto.CourseResponse;
import com.shannoncode.school.modules.course.dto.CourseSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseResponse create(CourseRequest courseRequest) {
        Course course = courseMapper.toEntity(courseRequest);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    @PreAuthorize("hasRole('ADMIN') or @courseSecurity.isEnrolled(principal.username, #id)")
    public CourseResponse findById(Long id) {
        return courseRepository.findById(id)
            .map(courseMapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
            .map(courseMapper::toResponse);
    }

    public Page<CourseResponse> searchCourses(CourseSearchRequest searchRequest, Pageable pageable) {
        Specification<Course> spec = CourseSpecifications.withFilters(searchRequest);

        return courseRepository.findAll(spec, pageable)
            .map(courseMapper::toResponse);
    }

    @Transactional
    public CourseResponse partialUpdate(Long id, CoursePatchRequest patchRequest) {
        Course existingCourse = courseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        courseMapper.updateEntityFromPatch(patchRequest, existingCourse);

        return courseMapper.toResponse(courseRepository.save(existingCourse));
    }

    @Transactional
    public void delete(Long id) {
        courseRepository.deleteById(id);
    }
}
