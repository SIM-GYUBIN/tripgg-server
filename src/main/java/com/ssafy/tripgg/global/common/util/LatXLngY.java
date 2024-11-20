package com.ssafy.tripgg.global.common.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LatXLngY {
    private double lat; // 위도
    private double lng; // 경도
    private double x;   // 격자 X
    private double y;   // 격자 Y
}
