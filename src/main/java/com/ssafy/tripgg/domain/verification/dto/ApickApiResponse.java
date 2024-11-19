package com.ssafy.tripgg.domain.verification.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApickApiResponse {
    private Data data;
    private Api api;

    @Getter
    @Setter @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private Map<String, Double> output;
        private Integer success;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Api {
        private Boolean success;
        private Integer cost;
        private Integer ms;
        private Integer plId;
    }
}
