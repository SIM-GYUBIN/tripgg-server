package com.ssafy.tripgg.domain.course.controller;

import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.dto.response.GPTAdviceResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.CourseDetailResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.CourseFinishResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.CompletedCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.InProgressCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.NotStartCourseResponse;
import com.ssafy.tripgg.domain.course.service.CourseService;
import com.ssafy.tripgg.global.common.ApiResponse;
import com.ssafy.tripgg.global.common.CustomPage;
import com.ssafy.tripgg.global.common.util.AuthenticationUtil;
import com.ssafy.tripgg.global.config.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping("/all")
    public ApiResponse<CustomPage<AllCourseResponse>> getAllCourses(
            @PageableDefault Pageable pageable,
            @Valid CourseRequest courseRequest) {

        return ApiResponse.success(courseService.getAllCourse(courseRequest, pageable));
    }

    @GetMapping("/not-started")
    public ApiResponse<CustomPage<NotStartCourseResponse>> getNotStartedCourses(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault Pageable pageable,
            @Valid CourseRequest courseRequest) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);

        return ApiResponse.success(courseService.getNotStartedCourse(userId, courseRequest, pageable));
    }

    @GetMapping("/in-progress")
    public ApiResponse<CustomPage<InProgressCourseResponse>> getInProgressCourses(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault Pageable pageable,
            @Valid CourseRequest courseRequest) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);

        return ApiResponse.success(courseService.getInProgressCourse(userId, courseRequest, pageable));
    }

    @GetMapping("/completed")
    public ApiResponse<CustomPage<CompletedCourseResponse>> getCompletedCourses(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault Pageable pageable,
            @Valid CourseRequest courseRequest) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);

        return ApiResponse.success(courseService.getCompletedCourse(userId, courseRequest, pageable));
    }

    @GetMapping("/{courseId}")
    public ApiResponse<CourseDetailResponse> getCourseDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("courseId") Long courseId) {

        Long userId = AuthenticationUtil.getCurrentUserIdCanNull(userPrincipal);
        return ApiResponse.success(courseService.getCourseDetail(userId, courseId));
    }

    @PostMapping("/{courseId}/challenge")
    public ApiResponse<String> challengeCourse(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("courseId") Long courseId) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);
        courseService.challengeCourse(userId, courseId);
        return ApiResponse.success("챌린지에 참여하였습니다.");
    }

    @PostMapping("/{courseId}/giveup")
    public ApiResponse<String> abandonCourse(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("courseId") Long courseId) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);
        courseService.abandonCourse(userId, courseId);
        return ApiResponse.success("챌린지를 포기하였습니다.");
    }

    @PostMapping("/{courseId}/finish")
    public ApiResponse<CourseFinishResponse> finishCourse(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("courseId") Long courseId) {

        Long userId = AuthenticationUtil.getCurrentUserId(userPrincipal);
        return ApiResponse.success(courseService.finishCourse(userId, courseId));
    }

    @GetMapping("/{courseId}/gpt")
    public ApiResponse<GPTAdviceResponse> getWeatherAndGPTAdvice(
            @PathVariable("courseId") Long courseId) {

        return ApiResponse.success(courseService.getWeatherAndGPTAdvice(courseId));
    }
}
