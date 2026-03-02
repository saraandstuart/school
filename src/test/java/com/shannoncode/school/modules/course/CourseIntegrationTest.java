package com.shannoncode.school.modules.course;

import com.shannoncode.school.BaseIntegrationTest;
import com.shannoncode.school.modules.course.dto.CourseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

class CourseIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("POST /api/v1/course should create a course and return 201 with Location header")
    void createCourse_Success() {
        var request = new CourseRequest("Linear Algebra", "IGCSE", "Maths");

        var result = mvc.post()
            .uri("/api/v1/course")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(request))
            .exchange();

        result.assertThat()
            .hasStatus(HttpStatus.CREATED)
            .hasHeader("Location", "http://localhost/api/v1/course/1")
            .bodyJson()
            .hasPathSatisfying("$.name", name -> name.assertThat().isEqualTo("Linear Algebra"))
            .hasPathSatisfying("$.id", id -> id.assertThat().isNotNull());
    }

    @Test
    @DisplayName("POST /api/v1/course should return 403 when user is not an ADMIN")
    void createCourse_Forbidden() {
        var request = new CourseRequest("Hack Course", "fake level", "Should fail");

        mvc.post()
            .uri("/api/v1/course")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT")))
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(request))
            .exchange()
            .assertThat()
            .hasStatus(HttpStatus.FORBIDDEN)
            .bodyJson().extractingPath("$.message").isEqualTo("Insufficient permissions");
    }

    @Test
    @DisplayName("Should allow valid CORS origin")
    void testCors_ValidOrigin() {
        mvc.post()
            .uri("/api/v1/course")
            .header("Origin", "http://localhost:3000")
            .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
            .exchange()
            .assertThat()
            .hasHeader("Access-Control-Allow-Origin", "http://localhost:3000");
    }
}