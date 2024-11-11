package com.ssafy.tripgg.domain.course.service;

import com.ssafy.tripgg.domain.course.dto.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import com.ssafy.tripgg.domain.course.repository.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public List<AllCourseResponse> getAllCourse(CourseRequest courseRequest) {

        Integer regionCode = courseRequest.getRegion() == Region.ALL
                ? null
                : courseRequest.getRegion().ordinal();

        // 기본 페이징
        Pageable pageable = PageRequest.of(
                courseRequest.getPage(),
                courseRequest.getSize()
        );


        List<Course> allCourse = courseRepository.findAllCourseByCondition(regionCode, courseRequest.getOrder().name(), pageable)
                .getContent();

        return allCourse.stream()
                .map(AllCourseResponse::from)
                .toList();
    }
}


