package com.shannoncode.school.common.mapper;

import com.shannoncode.school.modules.course.Course;
import com.shannoncode.school.modules.course.dto.CoursePatchRequest;
import com.shannoncode.school.modules.course.dto.CourseRequest;
import com.shannoncode.school.modules.course.dto.CourseResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper
public interface CourseMapper {

    CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseRequest courseRequest);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromPatch(CoursePatchRequest dto, @MappingTarget Course entity);
}
