package com.shannoncode.school.modules.course.dto;

public record CourseSearchRequest(
    String name,
    String level,
    String subject
) {
}
