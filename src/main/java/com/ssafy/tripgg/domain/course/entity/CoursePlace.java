package com.ssafy.tripgg.domain.course.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_places")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(nullable = false)
    private Integer sequence;

    @Builder
    public CoursePlace(Course course, Place place, Integer sequence) {
        this.course = course;
        this.place = place;
        this.sequence = sequence;
    }
}
