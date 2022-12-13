package com.dpm.winwin.api.common.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.dpm.winwin.api.common.error.enums.ErrorMessage;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3FileService implements FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    public String upload(MultipartFile multipartFile, Long memberId, String imageType) {
        String savedFileName = getSavedFileName(multipartFile, memberId, imageType);
        ObjectMetadata metadata = new ObjectMetadata();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(bucketName, savedFileName, inputStream, metadata);
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new BusinessException(ErrorMessage.INVALID_FILE_UPLOAD);
        }
        return getPath(savedFileName);
    }

    private String getSavedFileName(MultipartFile multipartFile, Long memberId, String imageType) {
        return String.format("member%s/%s/%s-%s",
                memberId, imageType, getRandomUUID(), multipartFile.getOriginalFilename());
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private String getPath(String savedFileName) {
        return defaultUrl + "/" + savedFileName;
    }
}
