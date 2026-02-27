package com.shannoncode.school.common.mapper;

import com.shannoncode.school.modules.course.dto.CourseRequest;
import com.shannoncode.school.modules.course.dto.CourseResponse;
import com.shannoncode.school.modules.course.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CourseMapper {

    CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseRequest courseRequest);
}
