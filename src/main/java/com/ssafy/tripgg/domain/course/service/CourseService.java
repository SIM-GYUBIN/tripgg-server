package com.ssafy.tripgg.domain.course.service;

import com.ssafy.tripgg.domain.course.dto.CourseRequest;
import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;
import com.ssafy.tripgg.domain.course.dto.query.InProgressCourseQuery;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.CourseDetailResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.CourseFinishResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.AllCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.CompletedCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.InProgressCourseResponse;
import com.ssafy.tripgg.domain.course.dto.response.course_list.NotStartCourseResponse;
import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.entity.CourseProgress;
import com.ssafy.tripgg.domain.course.entity.enums.ProgressStatus;
import com.ssafy.tripgg.domain.course.repository.CourseProgressRepository;
import com.ssafy.tripgg.domain.course.repository.CourseRepository;
import com.ssafy.tripgg.domain.user.entity.User;
import com.ssafy.tripgg.domain.user.repository.UserRepository;
import com.ssafy.tripgg.domain.verification.entity.PlaceVerification;
import com.ssafy.tripgg.domain.verification.repository.PlaceVerificationRepository;
import com.ssafy.tripgg.global.common.CustomPage;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseProgressRepository courseProgressRepository;
    private final UserRepository userRepository;
    private final PlaceVerificationRepository placeVerificationRepository;

    public CustomPage<AllCourseResponse> getAllCourse(CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findLatestCourses(regionCode, pageable);
            case POPULAR -> courseRepository.findPopularCourses(regionCode, pageable);
        };

        Page<AllCourseResponse> allCourseResponses = courses.map(AllCourseResponse::of);
        return new CustomPage<>(allCourseResponses);
    }

    public CustomPage<NotStartCourseResponse> getNotStartedCourse(Long userId, CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findNotStartedLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findNotStartedPopularCourses(userId, regionCode, pageable);
        };

        Page<NotStartCourseResponse> notStartCourseResponses = courses.map(NotStartCourseResponse::of);
        return new CustomPage<>(notStartCourseResponses);
    }

    public CustomPage<InProgressCourseResponse> getInProgressCourse(Long userId, @Valid CourseRequest courseRequest, Pageable pageable) {
        Integer regionCode = courseRequest.getRegion().getCode();

        Page<InProgressCourseQuery> inProgressCourseQuery = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findInProgressLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findInProgressPopularCourses(userId, regionCode, pageable);
        };

        Page<InProgressCourseResponse> inProgressCourseResponses = inProgressCourseQuery.map(InProgressCourseResponse::of);
        return new CustomPage<>(inProgressCourseResponses);
    }

    public CustomPage<CompletedCourseResponse> getCompletedCourse(Long userId, @Valid CourseRequest courseRequest, Pageable pageable) {

        Integer regionCode = courseRequest.getRegion().getCode();

        Page<Course> courses = switch (courseRequest.getOrderBy()) {
            case LATEST -> courseRepository.findCompletedLatestCourses(userId, regionCode, pageable);
            case POPULAR -> courseRepository.findCompletedPopularCourses(userId, regionCode, pageable);
        };

        Page<CompletedCourseResponse> completedCourseResponses = courses.map(CompletedCourseResponse::of);
        return new CustomPage<>(completedCourseResponses);
    }

    public CourseDetailResponse getCourseDetail(Long userId, Long courseId) {
        CourseDetailQuery courseDetail = courseRepository.findCourseDetailById(courseId,
                Optional.ofNullable(userId).orElse(-1L));

        return CourseDetailResponse.of(courseDetail);
    }

    public void challengeCourse(Long userId, Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_NOT_FOUND));

        Optional<CourseProgress> existingProgress = courseProgressRepository.findByUserAndCourse(user, course);

        if (existingProgress.isEmpty()) {
            CourseProgress courseProgress = CourseProgress.builder()
                    .user(user)
                    .course(course)
                    .build();

            courseProgressRepository.save(courseProgress);

        } else {
            CourseProgress courseProgress = existingProgress.get();

            switch (courseProgress.getStatus()) {
                case IN_PROGRESS -> throw new BusinessException(ErrorCode.COURSE_ALREADY_IN_PROGRESS);
                case COMPLETED -> throw new BusinessException(ErrorCode.COURSE_ALREADY_COMPLETED);
                case ABANDONED -> courseProgress.reChallenge();
            }
        }
    }

    public void abandonCourse(Long userId, Long courseId) {

        CourseProgress courseProgress = courseProgressRepository.findByUser_IdAndCourse_Id(userId, courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_PROCESS_NOT_FOUND));

        if (courseProgress.getStatus() != ProgressStatus.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.COURSE_NOT_IN_PROGRESS);
        }

        courseProgress.abandon();
    }

    public CourseFinishResponse finishCourse(Long userId, Long courseId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        CourseProgress courseProgress = courseProgressRepository.findByUser_IdAndCourse_Id(userId, courseId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COURSE_PROCESS_NOT_FOUND));

        if (courseProgress.getStatus() != ProgressStatus.IN_PROGRESS) {
            throw new BusinessException(ErrorCode.COURSE_NOT_IN_PROGRESS);
        }

        Long courseProgressId = courseProgress.getId();
        List<PlaceVerification> verificationList = placeVerificationRepository.findByCourseProgress_Id(courseProgressId);


        int score = 0;
        for (PlaceVerification verification : verificationList) {
            score += 10;
            if (verification.getPhotoVerified()) {
                score += 20;
            }
        }

        courseProgress.complete();
        user.addPoints(score);

        return CourseFinishResponse.builder()
                .gainedScore(score)
                .build();
    }
}


