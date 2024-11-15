package com.ssafy.tripgg.domain.course.repository;

import com.ssafy.tripgg.domain.course.entity.CourseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {
}
