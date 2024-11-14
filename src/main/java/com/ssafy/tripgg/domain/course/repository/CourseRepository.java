package com.ssafy.tripgg.domain.course.repository;

import com.ssafy.tripgg.domain.course.dto.query.InProgressCourseQuery;
import com.ssafy.tripgg.domain.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, CourseCustomRepository {

    @Query("SELECT c " +
            "FROM Course c " +
            "WHERE (:regionCode = 0 OR c.regionId = :regionCode) " +
            "ORDER BY c.createdAt DESC")
    Page<Course> findLatestCourses(
            @Param("regionCode") Integer regionCode,
            Pageable pageable
    );

    @Query("SELECT c " +
            "FROM Course c " +
            "LEFT JOIN CourseProgress cp ON c.id = cp.course.id " +
            "WHERE (:regionCode = 0 OR c.regionId = :regionCode) " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(cp.id) DESC")
    Page<Course> findPopularCourses(
            @Param("regionCode") Integer regionCode,
            Pageable pageable
    );

    @Query("SELECT c " +
            "FROM Course c " +
            "WHERE c.id NOT IN ( " +
            "    SELECT cp.course.id FROM CourseProgress cp WHERE cp.user.id = :userId " +
            ") " +
            "AND (:regionCode = 0 OR c.regionId = :regionCode) " +
            "ORDER BY c.createdAt DESC")
    Page<Course> findNotStartedLatestCourses(Long userId, Integer regionCode, Pageable pageable);

    @Query("SELECT c FROM Course c " +
            "LEFT JOIN CourseProgress cp1 ON c.id = cp1.course.id " +
            "WHERE c.id NOT IN ( " +
            "    SELECT cp2.course.id FROM CourseProgress cp2 WHERE cp2.user.id = :userId " +
            ") " +
            "AND (:regionCode = 0 OR c.regionId = :regionCode) " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(cp1.id) DESC")
    Page<Course> findNotStartedPopularCourses(
            @Param("userId") Long userId,
            @Param("regionCode") Integer regionCode,
            Pageable pageable
    );

    @Query("SELECT new com.ssafy.tripgg.domain.course.dto.query.InProgressCourseQuery(" +
            "c.id, c.title, c.description, c.regionId, c.thumbnailUrl, c.createdAt, " +
            "COUNT(DISTINCT cp2.id), " +  // totalPlaceNum
            "COUNT(DISTINCT pv.id), " +   // verifiedPlaceNum
            "cp1.startedAt) " +
            "FROM Course c " +
            "INNER JOIN CourseProgress cp1 ON c.id = cp1.course.id " +
            "LEFT JOIN CoursePlace cp2 ON c.id = cp2.course.id " +
            "LEFT JOIN PlaceVerification pv ON cp1.id = pv.courseProgress.id " +
            "WHERE cp1.user.id = :userId " +
            "AND cp1.status = 'IN_PROGRESS' " +
            "AND (:regionCode = 0 OR c.regionId = :regionCode) " +
            "GROUP BY c.id, c.title, c.description, c.thumbnailUrl, c.createdAt, cp1.startedAt " +
            "ORDER BY c.createdAt DESC")
    Page<InProgressCourseQuery> findInProgressLatestCourses(Long userId, Integer regionCode, Pageable pageable);

    @Query("SELECT new com.ssafy.tripgg.domain.course.dto.query.InProgressCourseQuery(" +
            "c.id, c.title, c.description, c.regionId,c.thumbnailUrl, c.createdAt, " +
            "COUNT(DISTINCT cp2.id), " +  // totalPlaceNum
            "COUNT(DISTINCT pv.id), " +   // verifiedPlaceNum
            "cp1.startedAt) " +
            "FROM Course c " +
            "INNER JOIN CourseProgress cp1 ON c.id = cp1.course.id " +
            "LEFT JOIN CoursePlace cp2 ON c.id = cp2.course.id " +
            "LEFT JOIN PlaceVerification pv ON cp1.id = pv.courseProgress.id " +
            "LEFT JOIN CourseProgress cpAll ON c.id = cpAll.course.id " + // 전체 진행 수를 위한 JOIN
            "WHERE cp1.user.id = :userId " +
            "AND cp1.status = 'IN_PROGRESS' " +
            "AND (:regionCode = 0 OR c.regionId = :regionCode) " +
            "GROUP BY c.id, c.title, c.description, c.thumbnailUrl, c.createdAt, cp1.startedAt " +
            "ORDER BY COUNT(DISTINCT cpAll.id) DESC")
    Page<InProgressCourseQuery> findInProgressPopularCourses(Long userId, Integer regionCode, Pageable pageable);


    @Query("SELECT c " +
            "FROM Course c " +
            "LEFT JOIN CourseProgress cp ON c.id = cp.course.id " +
            "WHERE cp.user.id = :userId " +
            "AND cp.status = 'COMPLETED' " +
            "AND (:regionCode = 0 OR c.regionId = :regionCode) " +
            "ORDER BY c.createdAt DESC")
    Page<Course> findCompletedLatestCourses(Long userId, Integer regionCode, Pageable pageable);


    @Query("SELECT c FROM Course c " +
            "LEFT JOIN CourseProgress cp1 ON c.id = cp1.course.id " +
            "WHERE cp1.user.id = :userId " +
            "AND cp1.status = 'COMPLETED' " +
            "AND (:regionCode = 0 OR c.regionId = :regionCode) " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(cp1.id) DESC")
    Page<Course> findCompletedPopularCourses(Long userId, Integer regionCode, Pageable pageable);
}
