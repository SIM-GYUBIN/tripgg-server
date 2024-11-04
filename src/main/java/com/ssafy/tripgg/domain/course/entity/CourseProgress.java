package com.ssafy.tripgg.domain.course.entity;

import com.ssafy.tripgg.domain.course.entity.enums.ProgressStatus;
import com.ssafy.tripgg.domain.user.entity.User;
import com.ssafy.tripgg.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_progresses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProgressStatus status;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Builder
    public CourseProgress(User user, Course course) {
        this.user = user;
        this.course = course;
        this.status = ProgressStatus.IN_PROGRESS;
        this.startedAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = ProgressStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void abandon() {
        this.status = ProgressStatus.ABANDONED;
        this.completedAt = LocalDateTime.now();
    }
}
