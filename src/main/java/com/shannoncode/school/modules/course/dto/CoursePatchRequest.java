package com.shannoncode.school.modules.course.dto;

public record CoursePatchRequest(
    String name,
    String level,
    String subject
) {
}
