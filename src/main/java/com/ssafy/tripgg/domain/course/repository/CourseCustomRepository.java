package com.ssafy.tripgg.domain.course.repository;

import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;

public interface CourseCustomRepository {
    CourseDetailQuery findCourseDetailById(Long courseId, Long userId);
}
