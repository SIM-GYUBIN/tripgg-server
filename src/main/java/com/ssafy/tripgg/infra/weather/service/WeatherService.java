package com.ssafy.tripgg.infra.weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tripgg.infra.weather.constants.WeatherConstants;
import com.ssafy.tripgg.infra.weather.dto.WeatherInfoDto;
import com.ssafy.tripgg.infra.weather.dto.WeatherResponseDto;
import com.ssafy.tripgg.global.common.util.GpsConverter;
import com.ssafy.tripgg.global.common.util.LatXLngY;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {
    private final GpsConverter gpsConverter;
    private final OkHttpClient okHttpClient;

    @Value("${api.weather.key}")
    private String serviceKey;

    public WeatherInfoDto getCurrentWeather(BigDecimal latitude, BigDecimal longitude) {
        // 1. 위경도를 기상청 좌표로 변환
        LatXLngY convertedCoord = gpsConverter.convertGRID_GPS(
                WeatherConstants.TO_GRID,
                latitude.doubleValue(),
                longitude.doubleValue()
        );

        // 2. 현재 시간 기준으로 발표시각 계산
        LocalDateTime now = LocalDateTime.now();
        String[] baseDateTime = calculateBaseDateTime(now);
        String baseDate = baseDateTime[0];
        String baseTime = baseDateTime[1];

        // 3. API 호출
        String url = buildUrl(baseDate, baseTime,
                (int) convertedCoord.getX(),
                (int) convertedCoord.getY());

        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            String responseBody = response.body().string();

            ObjectMapper mapper = new ObjectMapper();
            WeatherResponseDto weatherResponse = mapper.readValue(
                    responseBody,
                    WeatherResponseDto.class
            );

            return parseWeatherInfo(weatherResponse);

        } catch (Exception e) {
            log.error("날씨 정보 조회 실패", e);
            throw new BusinessException(ErrorCode.WEATHER_API_ERROR);
        }
    }

    private String[] calculateBaseDateTime(LocalDateTime now) {
        LocalDateTime baseTime = now.minusMinutes(10);

        // 현재 시각이 발표 후 10분 이내인 경우 1시간 전 데이터 사용
        if (now.getMinute() < 10) {
            baseTime = baseTime.minusHours(1);
        }

        // 00시 이전인 경우 전날 23시 데이터 사용
        if (baseTime.getHour() == 23 && now.getHour() == 0) {
            baseTime = baseTime.minusDays(1);
        }

        return new String[]{
                baseTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                baseTime.format(DateTimeFormatter.ofPattern("HHmm"))
        };
    }

    private String buildUrl(String baseDate, String baseTime, int nx, int ny) {
        return UriComponentsBuilder
                .fromHttpUrl(WeatherConstants.BASE_URL + WeatherConstants.ULTRA_NCT_URL)
                .queryParam("serviceKey", serviceKey)
                .queryParam("numOfRows", 10)
                .queryParam("pageNo", 1)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .build()
                .toUriString();
    }

    private WeatherInfoDto parseWeatherInfo(WeatherResponseDto response) {
        Map<String, String> weatherData = response.getResponse().getBody().getItems().getItem()
                .stream()
                .collect(Collectors.toMap(
                        WeatherResponseDto.Item::getCategory,
                        WeatherResponseDto.Item::getObsrValue
                ));

        return WeatherInfoDto.builder()
                .temperature(weatherData.get("T1H"))
                .precipitation(WeatherConstants.PTY_CODES.get(weatherData.get("PTY")))
                .rainAmount(weatherData.get("RN1"))
                .windSpeed(weatherData.get("WSD"))
                .build();
    }
}
