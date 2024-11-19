package com.ssafy.tripgg.infra.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ssafy.tripgg.global.error.ErrorCode;
import com.ssafy.tripgg.global.error.exception.BusinessException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Slf4j
@Data
public class S3ObjectStorage {

    private AmazonS3 amazonS3; //AmazonS3 config 미리 빈 주입
    private String bucket; //빈 주입 시 setter
    private String aiS3Url; //빈 주입 시 setter

    public S3ObjectStorage(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile multipartFile) {
        // UUID 이용해 고유한 파일명 생성
        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        try {
            amazonS3.putObject(bucket, fileName, multipartFile.getInputStream(), metadata);
        } catch (IOException ioException) {
            throw new BusinessException(ErrorCode.AWS_SERVER_ERROR);
        }
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public void deleteFile(String fileUrl) {
        try {
            // URL에서 객체 키 추출
            URI uri = new URI(fileUrl);
            // URL의 첫 번째 '/'를 제거하여 객체 키 얻기
            String key = uri.getPath().substring(1);

            // 파일 존재 여부 확인
            if (amazonS3.doesObjectExist(bucket, key)) {
                // S3에서 파일 삭제
                amazonS3.deleteObject(bucket, key);
                log.info("File deleted successfully: {}", key);
            } else { // file not found
                log.warn("File not found: {}", key);
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }
        } catch (Exception e) { //error
            log.error("Failed to delete file!: {}", fileUrl, e);
            throw new BusinessException(ErrorCode.AWS_SERVER_ERROR);
        }
    }
}
