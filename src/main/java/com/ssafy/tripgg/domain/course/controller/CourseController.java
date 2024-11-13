package com.ssafy.tripgg.domain.course.controller;

import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.dto.response.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.NotStartCourseResponse;
import com.ssafy.tripgg.domain.course.service.CourseService;
import com.ssafy.tripgg.global.common.ApiResponse;
import com.ssafy.tripgg.global.config.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/all")
    public ApiResponse<List<AllCourseResponse>> getAllCourses(
            @PageableDefault(size = 10) Pageable pageable,
            @Valid CourseRequest courseRequest) {

        return ApiResponse.success(courseService.getAllCourse(courseRequest, pageable));
    }

    @GetMapping("/not-started")
    public ApiResponse<List<NotStartCourseResponse>> getNotStartedCourses(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(size = 10) Pageable pageable,
            @Valid CourseRequest courseRequest) {

        Long userId = userPrincipal.getId();

        return ApiResponse.success(courseService.getNotStartedCourse(userId, courseRequest, pageable));
    }
}
