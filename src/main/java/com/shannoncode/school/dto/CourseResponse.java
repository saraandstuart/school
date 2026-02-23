package com.shannoncode.school.dto;

public record CourseResponse(
    Long id,
    String name,
    String level,
    String subject
) {
}
