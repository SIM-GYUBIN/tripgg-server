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

    @Query("SELECT c, COUNT(cp.id) as progressCount " +
            "FROM Course c " +
            "LEFT JOIN CourseProgress cp ON c.id = cp.course.id " +
            "WHERE (:regionCode IS NULL OR c.region_id = :regionCode) " +
            "GROUP BY c " +
            "ORDER BY " +
            "CASE WHEN :orderType = 'LATEST' THEN c.createdAt END DESC, " +
            "CASE WHEN :orderType = 'POPULAR' THEN progressCount END DESC")
    Page<Course> findAllCourseByCondition(
            @Param("regionCode") Integer regionCode,
            @Param("orderType") String orderType,
            Pageable pageable
    );
}
