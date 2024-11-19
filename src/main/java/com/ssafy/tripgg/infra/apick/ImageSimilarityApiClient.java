package com.ssafy.tripgg.infra.apick;

import com.ssafy.tripgg.domain.verification.dto.ApickApiResponse;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageSimilarityApiClient {

    @Value("${api.apick.url}")
    private String apiUrl;

    @Value("${api.apick.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public ApickApiResponse compareImages(MultipartFile baseImage, List<MultipartFile> compareImages) {
        try {
            // 설정값 확인
            log.info("API URL from config: {}", apiUrl);
            log.info("API Key from config: {}", apiKey);

            // MultipartFile 데이터 확인
            log.info("Base image name: {}, size: {}, content type: {}",
                    baseImage.getOriginalFilename(),
                    baseImage.getSize(),
                    baseImage.getContentType());

            compareImages.forEach(img ->
                    log.info("Compare image name: {}, size: {}, content type: {}",
                            img.getOriginalFilename(),
                            img.getSize(),
                            img.getContentType())
            );

            // RequestEntity 구성 확인
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
//            body.add("image", new MultipartInputStreamFileResource(baseImage.getInputStream(), baseImage.getOriginalFilename()));
            Resource resource1 = new FileSystemResource("C:/Users/SSAFY/Desktop/ganjeol.jpg");
            body.add("image", resource1);

//            for (int i = 0; i < compareImages.size(); i++) {
//                MultipartFile compareImage = compareImages.get(i);
//                body.add("compare_image" + (i + 1),
//                        new MultipartInputStreamFileResource(compareImage.getInputStream(), compareImage.getOriginalFilename()));
//            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("CL_AUTH_KEY", apiKey);
//            headers.set(HttpHeaders.ACCEPT, MediaType.ALL_VALUE);  // "*/*" 설정

            // 최종 요청 정보 확인
            log.info("Request headers: {}", headers);
            log.info("Request body keys: {}", body.keySet());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            return restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    ApickApiResponse.class
            ).getBody();

        } catch (Exception e) {
            log.error("API call error", e);
            throw new BusinessException(ErrorCode.API_CALL_FAILED);
        }
    }
}