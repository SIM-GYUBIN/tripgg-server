package com.ssafy.tripgg.domain.course.controller;

import com.ssafy.tripgg.domain.course.dto.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.service.CourseService;
import com.ssafy.tripgg.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<List<AllCourseResponse>> getAllCourses(CourseRequest courseRequest) {

        System.out.println(courseRequest);
        return ApiResponse.success(courseService.getAllCourse(courseRequest));
    }
}
