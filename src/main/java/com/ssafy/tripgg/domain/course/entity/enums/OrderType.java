package com.ssafy.tripgg.domain.course.entity.enums;

public enum OrderType {
    LATEST("최신순"),    // createdAt desc
    POPULAR("인기순");   // course_progresses count desc

    private final String description;

    OrderType(String description) {
        this.description = description;
    }
}
