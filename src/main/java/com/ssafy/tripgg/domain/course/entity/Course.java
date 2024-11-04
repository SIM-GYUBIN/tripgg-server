package com.ssafy.tripgg.domain.course.entity;

import com.ssafy.tripgg.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Column
    private String description;

    @Column(nullable = false, length = 50)
    private String region;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Builder
    public Course(String title, String description, String region, String thumbnailUrl) {
        this.title = title;
        this.description = description;
        this.region = region;
        this.thumbnailUrl = thumbnailUrl;
    }
}
