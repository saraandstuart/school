package com.shannoncode.school.controller;

import com.shannoncode.school.dto.CourseRequest;
import com.shannoncode.school.dto.CourseResponse;
import com.shannoncode.school.mapper.CourseMapper;
import com.shannoncode.school.model.Course;
import com.shannoncode.school.repository.CourseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/course")
@Tag(name = "Course", description = "API for managing courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Operation(summary = "Create a new course", description = "Validates input and persists a new course record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data provided"),
        @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        Course course = courseMapper.toEntity(courseRequest);
        Course savedCourse = courseRepository.save(course);
        CourseResponse courseResponse = courseMapper.toResponse(savedCourse);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(courseResponse.id())
            .toUri();

        return ResponseEntity.created(location).body(courseResponse);
    }

    @GetMapping
    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toResponseList(courses);
    }

}
