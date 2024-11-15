package com.ssafy.tripgg.domain.course.service;

import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;
import com.ssafy.tripgg.domain.course.dto.query.InProgressCourseQuery;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.CourseDetailResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.CompletedCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.InProgressCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.NotStartCourseResponse;
import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.repository.CourseRepository;
import com.ssafy.tripgg.global.common.CustomPage;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public CustomPage<AllCourseResponse> getAllCourse(CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findLatestCourses(regionCode, pageable);
            case POPULAR -> courseRepository.findPopularCourses(regionCode, pageable);
        };

        Page<AllCourseResponse> allCourseResponses = courses.map(AllCourseResponse::from);
        return new CustomPage<>(allCourseResponses);
    }

    public CustomPage<NotStartCourseResponse> getNotStartedCourse(Long userId, CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findNotStartedLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findNotStartedPopularCourses(userId, regionCode, pageable);
        };

        Page<NotStartCourseResponse> notStartCourseResponses = courses.map(NotStartCourseResponse::from);
        return new CustomPage<>(notStartCourseResponses);
    }

    public CustomPage<InProgressCourseResponse> getInProgressCourse(Long userId, @Valid CourseRequest courseRequest, Pageable pageable) {
        Integer regionCode = courseRequest.getRegion().getCode();

        Page<InProgressCourseQuery> inProgressCourseQuery = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findInProgressLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findInProgressPopularCourses(userId, regionCode, pageable);
        };

        Page<InProgressCourseResponse> inProgressCourseResponses = inProgressCourseQuery.map(InProgressCourseResponse::from);
        return new CustomPage<>(inProgressCourseResponses);
    }

    public CustomPage<CompletedCourseResponse> getCompletedCourse(Long userId, @Valid CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findCompletedLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findCompletedPopularCourses(userId, regionCode, pageable);
        };

        Page<CompletedCourseResponse> completedCourseResponses = courses.map(CompletedCourseResponse::from);
        return new CustomPage<>(completedCourseResponses);
    }

    public CourseDetailResponse getCourseDetail(Long userId, Long courseId) {
        CourseDetailQuery courseDetail = courseRepository.findCourseDetailById(courseId,
                Optional.ofNullable(userId).orElse(-1L));

        return CourseDetailResponse.builder()
                .id(courseDetail.getId())
                .title(courseDetail.getTitle())
                .description(courseDetail.getDescription())
                .region(courseDetail.getRegion())
                .status(courseDetail.getStatus())
                .places(courseDetail.getPlaces())
                .createdAt(courseDetail.getCreatedAt())
                .updatedAt(courseDetail.getUpdatedAt())
                .build();
    }
}


