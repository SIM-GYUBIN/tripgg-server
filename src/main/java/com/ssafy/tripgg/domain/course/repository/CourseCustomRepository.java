package com.ssafy.tripgg.domain.course.repository;

import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;
import com.ssafy.tripgg.domain.course.dto.query.LocAndPlacesQuery;

public interface CourseCustomRepository {
    CourseDetailQuery findCourseDetailById(Long courseId, Long userId);

    LocAndPlacesQuery findLocAndPlacesByCourseId(Long courseId);
}
