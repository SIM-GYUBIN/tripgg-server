package com.ssafy.tripgg.domain.course.entity.enums;

public enum Region {
    ALL("전국"),
    SEOUL("서울"),
    INCHEON("인천"),
    DAEJEON("대전"),
    DAEGU("대구"),
    GWANGJU("광주"),
    BUSAN("부산"),
    ULSAN("울산"),
    SEJONG("세종"),
    GYEONGGI("경기"),
    GANGWON("강원"),
    CHUNGBUK("충북"),
    CHUNGNAM("충남"),
    JEONBUK("전북"),
    JEONNAM("전남"),
    GYEONGBUK("경북"),
    GYEONGNAM("경남"),
    JEJU("제주");

    private final String region;

    Region(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
