package com.ssafy.tripgg.domain.course.service;

import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.dto.response.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.NotStartCourseResponse;
import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<AllCourseResponse> getAllCourse(CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findLatestCourses(regionCode, pageable);
            case POPULAR -> courseRepository.findPopularCourses(regionCode, pageable);
        };

        return courses.stream()
                .map(AllCourseResponse::from)
                .toList();
    }

    public List<NotStartCourseResponse> getNotStartedCourse(Long userId, CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findNotStartedLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findNotStartedPopularCourses(userId, regionCode, pageable);
        };

        return courses.stream()
                .map(NotStartCourseResponse::from)
                .toList();
    }
}


