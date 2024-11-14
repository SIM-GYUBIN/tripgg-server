package com.ssafy.tripgg.domain.course.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseCustomRepository{

    private final JPAQueryFactory queryFactory;
}
