package com.ssafy.tripgg.infra.weather.constants;

import java.util.Map;

public class WeatherConstants {
    public static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0";
    public static final String ULTRA_NCT_URL = "/getUltraSrtNcst";

    public static final int TO_GRID = 0;
    public static final int TO_GPS = 1;

    // 날씨 상태 코드
    public static final Map<String, String> PTY_CODES = Map.of(
            "0", "없음",
            "1", "비",
            "2", "비/눈",
            "3", "눈",
            "5", "빗방울",
            "6", "빗방울눈날림",
            "7", "눈날림"
    );
}
