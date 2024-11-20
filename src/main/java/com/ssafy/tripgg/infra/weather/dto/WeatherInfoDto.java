package com.ssafy.tripgg.infra.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherInfoDto {
    private String temperature;    // 기온
    private String precipitation;  // 강수형태
    private String rainAmount;     // 강수량
    private String windSpeed;      // 풍속

    public String toKoreanString() {
        return String.format(
                "현재 기온은 %s도이며, 강수는 %s입니다. 시간당 강수량은 %smm이고, 풍속은 %sm/s입니다.",
                temperature,
                precipitation,
                rainAmount,
                windSpeed
        );
    }
}
