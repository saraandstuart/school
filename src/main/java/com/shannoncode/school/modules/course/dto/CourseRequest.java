package com.shannoncode.school.modules.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Information required to create a new Course")
public record CourseRequest(
    @Schema(description = "Name of the Course", example = "Linear Algebra")
    @NotBlank(message = "name is required") @Size(max = 100) String name,

    @Schema(description = "Level of the Course", example = "IGCSE")
    @NotBlank(message = "level is required") @Size(max = 100) String level,

    @Schema(description = "Subject of the Course", example = "Mathematics")
    @NotBlank(message = "subject is required") @Size(max = 100) String subject
) {
}
