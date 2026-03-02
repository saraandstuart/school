package com.shannoncode.school.modules.course;

import com.shannoncode.school.modules.course.dto.CourseSearchRequest;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecifications {

    public static Specification<Course> withFilters(CourseSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.name() != null) {
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + request.name().toLowerCase() + "%"));
            }

            if (request.level() != null) {
                predicates.add(cb.like(cb.lower(root.get("level")), "%" + request.level().toLowerCase() + "%"));
            }

            if (request.subject() != null) {
                predicates.add(cb.like(cb.lower(root.get("subject")), "%" + request.subject().toLowerCase() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
