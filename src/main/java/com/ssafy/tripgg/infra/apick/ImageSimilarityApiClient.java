package com.ssafy.tripgg.infra.apick;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.tripgg.domain.verification.dto.ApickApiResponse;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageSimilarityApiClient {

    @Value("${api.apick.url}")
    private String apiUrl;

    @Value("${api.apick.key}")
    private String apiKey;

    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;

    public ApickApiResponse compareImages(MultipartFile baseImage, List<MultipartFile> compareImages) throws IOException {
        byte[] baseImageBytes = baseImage.getBytes();

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", baseImage.getOriginalFilename(),
                        RequestBody.create(baseImageBytes, MediaType.parse("image/*")));

        // 비교 이미지 추가
        for (int i = 0; i < compareImages.size(); i++) {
            MultipartFile compareImage = compareImages.get(i);
            bodyBuilder.addFormDataPart("compare_image" + (i + 1),
                    compareImage.getOriginalFilename(),
                    RequestBody.create(compareImage.getBytes(), MediaType.parse("image/*")));
        }

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("CL_AUTH_KEY", apiKey)
                .post(bodyBuilder.build())
                .build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new BusinessException(ErrorCode.API_CALL_FAILED);
            }

            assert response.body() != null;
            return objectMapper.readValue(response.body().string(), ApickApiResponse.class);
        }
    }
}