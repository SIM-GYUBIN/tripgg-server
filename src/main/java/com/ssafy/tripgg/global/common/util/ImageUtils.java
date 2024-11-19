package com.ssafy.tripgg.global.common.util;

import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUtils {

    private final RestTemplate restTemplate;

    public MultipartFile urlToMultipartFile(String imageUrl) {
        try {
            byte[] imageBytes = restTemplate.getForObject(imageUrl, byte[].class);
            if (imageBytes == null) {
                throw new BusinessException(ErrorCode.IMAGE_DOWNLOAD_FAILED);
            }

            String fileName = extractFileName(imageUrl);
            String contentType = determineContentType(fileName);

            return new CustomMultipartFile(imageBytes, fileName, contentType);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.IMAGE_CONVERSION_FAILED);
        }
    }

    private String extractFileName(String imageUrl) {
        return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
    }

    private String determineContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return switch (extension) {
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            default -> "application/octet-stream";
        };
    }
}
