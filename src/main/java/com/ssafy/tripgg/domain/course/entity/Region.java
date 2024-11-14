package com.ssafy.tripgg.domain.course.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "regions")
public class Region {  // 또는 RegionData
    @Id
    private Integer id;
    private String name;
}
