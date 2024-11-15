package com.ssafy.tripgg.domain.course.repository;

import com.ssafy.tripgg.domain.course.entity.Course;
import com.ssafy.tripgg.domain.course.entity.CourseProgress;
import com.ssafy.tripgg.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {
    Optional<CourseProgress> findByUserAndCourse(User user, Course course);
}
