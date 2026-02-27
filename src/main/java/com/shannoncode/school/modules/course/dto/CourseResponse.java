package com.shannoncode.school.modules.course.dto;

public record CourseResponse(
    Long id,
    String name,
    String level,
    String subject
) {
}
