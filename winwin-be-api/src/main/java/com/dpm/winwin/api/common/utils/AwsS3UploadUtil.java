package com.dpm.winwin.api.common.utils;

import static com.dpm.winwin.api.common.error.enums.ErrorMessage.INVALID_FILE_UPLOAD;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.dpm.winwin.api.common.error.exception.custom.BusinessException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3UploadUtil {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    public String upload(MultipartFile multipartFile, String directory) throws IOException {
        String savedFileName = getSavedFileName(multipartFile, directory);
        ObjectMetadata metadata = new ObjectMetadata();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(bucketName, savedFileName, inputStream, metadata);
        } catch (IOException e) {
            log.error("Failed to upload image", e);
            throw new BusinessException(INVALID_FILE_UPLOAD);
        }
        return defaultUrl;
    }

    private String getSavedFileName(MultipartFile multipartFile, String directory) {
        return directory + "/" + UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
    }

}
