package com.ssafy.tripgg.domain.course.entity;

import com.ssafy.tripgg.domain.course.entity.enums.ProgressStatus;
import com.ssafy.tripgg.domain.user.entity.User;
import com.ssafy.tripgg.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "course_progresses")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Builder.Default
    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProgressStatus status = ProgressStatus.IN_PROGRESS;

    @Builder.Default
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void complete() {
        this.status = ProgressStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public void abandon() {
        this.status = ProgressStatus.ABANDONED;
        this.completedAt = LocalDateTime.now();
    }
}
