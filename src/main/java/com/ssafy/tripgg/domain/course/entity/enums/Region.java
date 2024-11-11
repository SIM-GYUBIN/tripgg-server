package com.ssafy.tripgg.domain.course.entity.enums;

public enum Region {
    ALL("전국", 0),
    SEOUL("서울",1),
    INCHEON("인천",2),
    DAEJEON("대전",3),
    DAEGU("대구",4),
    GWANGJU("광주",5),
    BUSAN("부산",6),
    ULSAN("울산",7),
    SEJONG("세종",8),
    GYEONGGI("경기",31),
    GANGWON("강원",32),
    CHUNGBUK("충북",33),
    CHUNGNAM("충남",34),
    GYEONGBUK("경북",35),
    GYEONGNAM("경남",36),
    JEONBUK("전북",37),
    JEONNAM("전남",38),
    JEJU("제주",39);

    private final String region;
    private final Integer code;

    Region(String region, Integer code) {
        this.region = region;
        this.code = code;
    }

    public String getRegion() {
        return region;
    }

    public Integer getCode() {
        return code;
    }
}
