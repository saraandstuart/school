package com.shannoncode.school.mapper;

import com.shannoncode.school.dto.CourseRequest;
import com.shannoncode.school.dto.CourseResponse;
import com.shannoncode.school.model.Course;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CourseMapper {

    CourseResponse toResponse(Course course);

    List<CourseResponse> toResponseList(List<Course> courses);

    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseRequest courseRequest);
}
