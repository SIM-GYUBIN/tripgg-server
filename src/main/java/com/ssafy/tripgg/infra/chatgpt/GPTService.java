package com.ssafy.tripgg.infra.chatgpt;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tripgg.domain.course.dto.query.PlacesQuery;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import com.ssafy.tripgg.infra.weather.dto.WeatherInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GPTService {

    @Value("${api.chatgpt.key}")
    private String apiKey;

    private final OkHttpClient okHttpClient;
    private final ObjectMapper objectMapper;

    private static final String GPT_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json");

    public String generateTravelGuide(WeatherInfoDto weather, List<PlacesQuery> places) {
        try {
            String prompt = createPrompt(weather, places);
            RequestBody body = createRequestBody(prompt);

            Request request = new Request.Builder()
                    .url(GPT_API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            assert response.body() != null;
            return parseGPTResponse(response.body().string());

        } catch (Exception e) {
            log.error("GPT API 호출 실패", e);
            throw new BusinessException(ErrorCode.GPT_API_ERROR);
        }
    }

    private String createPrompt(WeatherInfoDto weather, List<PlacesQuery> places) {
        StringBuilder placesInfo = new StringBuilder();
        for (PlacesQuery place : places) {
            placesInfo.append("- ").append(place.getName())
                    .append(": ").append(place.getDescription())
                    .append("\n");
        }

        return String.format("""
                        관광지들에 대해 현재 날씨를 고려한 간단한 여행 가이드를 제공해주세요.
                        
                        현재 날씨: %s
                        
                        관광지 정보:
                        %s
                        
                        다음 조건을 지켜주세요:
                        1. 200자 이내로 작성
                        2. 날씨를 고려한 실용적인 조언 포함
                        3. 관광지별 핵심 포인트만 간단히 설명
                        """,
                weather.toKoreanString(),
                placesInfo
        );
    }

    private RequestBody createRequestBody(String prompt) throws JsonProcessingException {
        Map<String, Object> messageSystem = new LinkedHashMap<>();
        messageSystem.put("role", "system");
        messageSystem.put("content", "당신은 여행 가이드입니다. 간단명료하게 답변해주세요.");

        Map<String, Object> messageUser = new LinkedHashMap<>();
        messageUser.put("role", "user");
        messageUser.put("content", prompt);

        Map<String, Object> jsonBody = new LinkedHashMap<>();
        jsonBody.put("model", "gpt-4o-mini");
        jsonBody.put("messages", Arrays.asList(messageSystem, messageUser));
        jsonBody.put("max_tokens", 500);
        jsonBody.put("temperature", 0.7);

        // ObjectMapper로 JSON 문자열 생성
        String jsonString = objectMapper.writeValueAsString(jsonBody);

        return RequestBody.create(jsonString, JSON);
    }

    private String parseGPTResponse(String response) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(response);
        String content = jsonNode.get("choices")
                .get(0)
                .get("message")
                .get("content")
                .asText("");

        return content.replaceAll("\n", "<br>")
                .replaceAll("\\*\\*(.+?)\\*\\*", "<strong>$1</strong>");
    }
}
