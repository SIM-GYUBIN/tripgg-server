package com.ssafy.tripgg.domain.course.entity.enums;

import lombok.Getter;

@Getter
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

    private final String koreanName;
    private final Integer code;

    Region(String koreanName, Integer code) {
        this.koreanName = koreanName;
        this.code = code;
    }

    public static Region findByCode(Integer code) {
        for (Region region : values()) {
            if (region.getCode().equals(code)) {
                return region;
            }
        }
        return null;
    }
}
