package com.shannoncode.school.modules.course;

import com.shannoncode.school.modules.course.dto.CourseRequest;
import com.shannoncode.school.modules.course.dto.CourseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    private final CourseService courseService;

    @Operation(summary = "Create a new course", description = "Validates input and persists a new course record")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data provided"),
        @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseRequest courseRequest) {
        CourseResponse courseResponse = courseService.create(courseRequest);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(courseResponse.id())
            .toUri();

        return ResponseEntity.created(location).body(courseResponse);
    }

    @GetMapping("/{id}")
    public CourseResponse getCourse(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(
        @ParameterObject @PageableDefault(
            size = 20,
            sort = "name",
            direction = Sort.Direction.DESC
        ) Pageable pageable) {
        return ResponseEntity.ok(courseService.getAllCourses(pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
