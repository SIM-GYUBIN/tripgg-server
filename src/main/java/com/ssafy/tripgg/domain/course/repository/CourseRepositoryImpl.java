package com.ssafy.tripgg.domain.course.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.tripgg.domain.course.dto.query.CourseDetailQuery;
import com.ssafy.tripgg.domain.course.dto.response.course_detail.PlaceResponse;
import com.ssafy.tripgg.domain.course.entity.QCourse;
import com.ssafy.tripgg.domain.course.entity.QCoursePlace;
import com.ssafy.tripgg.domain.course.entity.QCourseProgress;
import com.ssafy.tripgg.domain.place.entity.QPlace;
import com.ssafy.tripgg.domain.course.entity.enums.Region;
import com.ssafy.tripgg.domain.verification.entity.QPlaceVerification;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseCustomRepository {
    private final JPAQueryFactory queryFactory;

    public CourseDetailQuery findCourseDetailById(Long courseId, Long userId) {
        QCourse course = QCourse.course;
        QCoursePlace coursePlace = QCoursePlace.coursePlace;
        QPlace place = QPlace.place;
        QCourseProgress courseProgress = QCourseProgress.courseProgress;
        QPlaceVerification placeVerification = QPlaceVerification.placeVerification;
//        QRegion region = QRegion.regionEntity;

        // 기본 정보와 장소 정보를 한 번에 조회
        List<Tuple> results = queryFactory
                .select(
                        course.id,
                        course.title,
                        course.description,
                        course.regionId,
                        course.createdAt,
                        course.updatedAt,
                        coursePlace.sequence,
                        place.id,
                        place.name,
                        place.description,
                        place.latitude,
                        place.longitude,
                        place.address,
                        place.imageUrl,
                        courseProgress.status,
                        placeVerification.id.isNotNull().as("isVerified"),
                        placeVerification.photoVerified
                )
                .from(course)
                .leftJoin(coursePlace).on(coursePlace.course.eq(course))
                .leftJoin(place).on(coursePlace.place.eq(place))
                .leftJoin(courseProgress).on(courseProgress.course.eq(course)
                        .and(courseProgress.user.id.eq(userId)))
                .leftJoin(placeVerification).on(placeVerification.courseProgress.eq(courseProgress)
                        .and(placeVerification.place.eq(place)))
                .where(course.id.eq(courseId))
                .orderBy(coursePlace.sequence.asc())
                .fetch();

        if (results.isEmpty()) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }

        // 첫 번째 row에서 코스 기본 정보 추출
        Tuple firstRow = results.get(0);

        // Region enum으로 변환
        Region region = Objects.requireNonNull(Region.findByCode(firstRow.get(course.regionId)));

        // 장소 정보 매핑
        List<PlaceResponse> places = results.stream()
                .filter(row -> row.get(place.id) != null)
                .map(row -> PlaceResponse.builder()
                        .id(row.get(place.id))
                        .name(row.get(place.name))
                        .description(row.get(place.description))
                        .latitude(row.get(place.latitude))
                        .longitude(row.get(place.longitude))
                        .address(row.get(place.address))
                        .imageUrl(row.get(place.imageUrl))
                        .sequence(row.get(coursePlace.sequence))
                        .isVerified(Boolean.TRUE.equals(row.get(15, Boolean.class)))
                        .isPhotoVerified(Boolean.TRUE.equals(row.get(placeVerification.photoVerified)))
                        .build())
                .toList();

        // CourseDetailQuery 생성
        return CourseDetailQuery.builder()
                .id(firstRow.get(course.id))
                .title(firstRow.get(course.title))
                .description(firstRow.get(course.description))
                .region(region.getKoreanName())
                .status(firstRow.get(courseProgress.status))
                .places(places)
                .createdAt(firstRow.get(course.createdAt))
                .updatedAt(firstRow.get(course.updatedAt))
                .build();
    }
}