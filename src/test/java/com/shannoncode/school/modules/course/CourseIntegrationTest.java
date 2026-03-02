package com.shannoncode.school.modules.course;

import com.shannoncode.school.BaseIntegrationTest;
import com.shannoncode.school.modules.course.dto.CourseRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CourseIntegrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("POST /api/v1/course should create a course and return 201 with Location header")
    void createCourse_Success() throws Exception {
        var request = new CourseRequest("Linear Algebra", "IGCSE", "Maths");

        mockMvc.perform(post("/api/v1/course")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(request)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.name").value("Linear Algebra"))
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("POST /api/v1/course should return 403 when user is not an ADMIN")
    void createCourse_Forbidden() throws Exception {
        var request = new CourseRequest("Hack Course", "fake level", "Should fail");

        mockMvc.perform(post("/api/v1/course")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))) // Non-admin
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJson(request)))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("Insufficient permissions"));
    }

    @Test
    @DisplayName("Should allow valid CORS origin")
    void testCors_ValidOrigin() throws Exception {
        mockMvc.perform(post("/api/v1/course")
                .header("Origin", "http://localhost:3000")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }
}