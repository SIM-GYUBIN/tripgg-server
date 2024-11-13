package com.ssafy.tripgg.domain.course.repository;

import com.ssafy.tripgg.domain.course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

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

    @Query("SELECT c FROM Course c " +
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
}
