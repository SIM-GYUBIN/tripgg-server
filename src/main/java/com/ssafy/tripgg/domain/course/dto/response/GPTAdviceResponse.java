package com.ssafy.tripgg.domain.course.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GPTAdviceResponse {
    private String weatherInfo;
    private String gptGuide;
}
